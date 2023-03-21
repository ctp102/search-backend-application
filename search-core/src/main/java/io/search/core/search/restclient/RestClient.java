package io.search.core.search.restclient;

import io.search.core.commons.form.PagingForm;
import io.search.core.search.form.SearchForm;
import io.search.core.search.response.SearchResponse;

public interface RestClient {

    <T> SearchResponse<T> searchBlog(SearchForm searchForm, PagingForm pagingForm);

}
