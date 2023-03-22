package io.search.core.search.service;


import io.search.core.CommonTest;
import io.search.core.commons.form.PagingForm;
import io.search.core.search.domain.SearchDomain;
import io.search.core.search.enums.PlatformType;
import io.search.core.search.facade.SearchRedissonLockFacade;
import io.search.core.search.form.SearchForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
class SearchServiceTest extends CommonTest {
    
    @Autowired
    private SearchService searchService;

    @Autowired
    private SearchRedissonLockFacade searchRedissonLockFacade;

    @BeforeEach
    @DisplayName("테스트 데이터 셋업")
    public void setUp() throws Exception {

        String query = "개발자";
        PlatformType platform = PlatformType.KAKAO;

        for (int i = 10; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                SearchDomain searchDomain = searchService.getSearchByQueryAndPlatform(query + i, platform);
                if (searchDomain == null) {
                    searchService.insertSearch(new SearchDomain(query + i, platform));
                } else {
                    searchDomain.increaseCount();
                }
            }
        }
    }

    @Test
    @DisplayName("인기검색어 상위 10개 조회 테스트")
    @Order(1)
    public void getHotSearchByQuery() throws Exception {

        List<SearchDomain> searchDomains = searchService.getHotTop10Search();

        assertThat(searchDomains.size()).isLessThanOrEqualTo(10);
    }

    @Test
    @DisplayName("검색어와 플랫폼으로 Search 엔티티 조회 테스트")
    @Order(2)
    public void getSearchByQueryAndPlatform() throws Exception {

        String query = "개발자1";
        PlatformType platform = PlatformType.KAKAO;

        SearchDomain searchDomain = searchService.getSearchByQueryAndPlatform(query, platform);

        assertThat(searchDomain).isNotNull();
    }

    @Test
    @DisplayName("블로그 검색 테스트")
    @Order(3)
    public void searchBlog() throws Exception {

        String query = "봄";
        SearchForm searchForm = new SearchForm(query);

        searchService.searchBlog(searchForm, new PagingForm());

        SearchDomain searchDomain = searchService.getSearchByQueryAndPlatform(query, PlatformType.KAKAO);

        assertThat(searchDomain.getCount()).isEqualTo(1);
    }

    @Test
    @DisplayName("같은 검색어로 블로그 2번 검색 후 count 증가 테스트")
    @Order(4)
    public void searchCountFor2Trial() throws Exception {

        String query = "여름";
        SearchForm searchForm = new SearchForm(query);

        searchService.searchBlog(searchForm, new PagingForm());
        searchService.searchBlog(searchForm, new PagingForm());

        SearchDomain searchDomain = searchService.getSearchByQueryAndPlatform(query, PlatformType.KAKAO);

        assertThat(searchDomain.getCount()).isEqualTo(2);
    }

    @Test
    @DisplayName("동시에 동일한 검색어를 100번 검색 후 count 검증 처리 테스트")
    @Order(5)
    public void searchCountFor100TrialSimultaneously() throws Exception {
        int threadCount = 100;
        ExecutorService executor = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executor.execute(() -> {
                try {
                    searchRedissonLockFacade.searchBlog(new SearchForm("가을"), new PagingForm());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        SearchDomain searchDomain = searchService.getSearchByQueryAndPlatform("가을", PlatformType.KAKAO);

        assertThat(searchDomain.getCount()).isEqualTo(100);
    }

}