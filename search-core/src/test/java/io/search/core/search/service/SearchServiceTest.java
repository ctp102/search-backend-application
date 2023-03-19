package io.search.core.search.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.search.core.CommonTest;
import io.search.core.commons.form.PagingForm;
import io.search.core.search.form.SearchForm;
import io.search.core.search.response.SearchResponse;
import io.search.core.search.response.kakao.KakaoBlogSearchResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class SearchServiceTest extends CommonTest {
    
    @Autowired
    private SearchService searchService;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    public void searchBlog() throws Exception {
        SearchForm searchForm = new SearchForm();
        searchForm.setQuery("개발자");
//        searchForm.setSort("recency");
        searchForm.setSort("accuracy");

        PagingForm pagingForm = new PagingForm();
        SearchResponse<KakaoBlogSearchResponse> response = searchService.searchBlog(searchForm, pagingForm);

        viewJson(response.getData().getMeta());
        viewJson(response.getData().getDocuments());
    }

}