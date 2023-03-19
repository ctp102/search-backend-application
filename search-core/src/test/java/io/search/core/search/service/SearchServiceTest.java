package io.search.core.search.service;


import io.search.core.CommonTest;
import io.search.core.commons.form.PagingForm;
import io.search.core.search.domain.SearchDomain;
import io.search.core.search.dto.SearchResultDto;
import io.search.core.search.enums.PlatformType;
import io.search.core.search.form.SearchForm;
import lombok.extern.slf4j.Slf4j;
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

    @Test
    public void getHotSearchByQuery() throws Exception {
        List<SearchDomain> searchDomain = searchService.getHotTop10Search();
        viewJson(searchDomain);
    }

    @Test
    public void getSearchByQueryAndPlatform() throws Exception {
        String query = "개발자";
        PlatformType platform = PlatformType.KAKAO;
        SearchDomain searchDomain = searchService.getSearchByQueryAndPlatform(query, platform);

        assertThat(searchDomain.getId()).isEqualTo(1);
    }

    @Test
    public void searchBlog() throws Exception {
        SearchForm searchForm = new SearchForm();
        searchForm.setQuery("개발자");
//        searchForm.setSort("recency");
        searchForm.setSort("accuracy");

        PagingForm pagingForm = new PagingForm();

        List<SearchResultDto> searchResults = searchService.searchBlog(searchForm, pagingForm);

        viewJson(searchResults);
    }

}