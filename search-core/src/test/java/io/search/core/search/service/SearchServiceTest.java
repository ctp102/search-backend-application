package io.search.core.search.service;


import io.search.core.CommonTest;
import io.search.core.commons.form.PagingForm;
import io.search.core.search.dto.SearchResultDto;
import io.search.core.search.form.SearchForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

class SearchServiceTest extends CommonTest {
    
    @Autowired
    private SearchService searchService;

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