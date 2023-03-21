package io.search.core.search.service;


import io.search.core.CommonTest;
import io.search.core.commons.form.PagingForm;
import io.search.core.search.domain.SearchDomain;
import io.search.core.search.dto.SearchResultDto;
import io.search.core.search.enums.PlatformType;
import io.search.core.search.form.SearchForm;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@Transactional
class SearchServiceTest extends CommonTest {
    
    @Autowired
    private SearchService searchService;

    @BeforeEach
    @DisplayName("")
    public void setUp() throws Exception {

        String query = "개발자";
        PlatformType platform = PlatformType.KAKAO;

        for (int i = 10; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                SearchDomain findSearchDomain = searchService.getSearchByQueryAndPlatform(query + i, platform);
                searchService.insertSearch(findSearchDomain, query + i, platform);
            }
        }

    }

    @Test
    @DisplayName("인기검색어 상위 10개 조회 테스트")
    public void getHotSearchByQuery() throws Exception {

        List<SearchDomain> searchDomains = searchService.getHotTop10Search();

        assertThat(searchDomains.size()).isLessThanOrEqualTo(10);
    }

    @Test
    @DisplayName("검색어와 플랫폼으로 Search 엔티티 조회 테스트")
    public void getSearchByQueryAndPlatform() throws Exception {

        String query = "개발자1";
        PlatformType platform = PlatformType.KAKAO;

        SearchDomain searchDomain = searchService.getSearchByQueryAndPlatform(query, platform);

        assertThat(searchDomain).isNotNull();
    }

    @Test
    @DisplayName("블로그 검색 테스트")
    public void searchBlog() throws Exception {

        String query = "개발자";
        SearchForm searchForm = new SearchForm(query);

        List<SearchResultDto> searchResults = searchService.searchBlog(searchForm, new PagingForm());

        assertThat(searchResults.size()).isGreaterThan(0);
    }

    @Test
    @DisplayName("같은 검색어로 블로그 검색 후 count 증가 테스트")
    public void SearchCount() throws Exception {

        String query = "개발자";
        SearchForm searchForm = new SearchForm(query);

        searchService.searchBlog(searchForm, new PagingForm());
        searchService.searchBlog(searchForm, new PagingForm());

        SearchDomain searchDomain = searchService.getSearchByQueryAndPlatform(query, PlatformType.KAKAO);

        assertThat(searchDomain.getCount()).isEqualTo(2);
    }

}