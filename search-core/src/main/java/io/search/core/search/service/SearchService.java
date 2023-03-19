package io.search.core.search.service;

import io.search.core.commons.form.PagingForm;
import io.search.core.search.dto.SearchResultDto;
import io.search.core.search.form.SearchForm;
import io.search.core.search.response.KakaoBlogSearchResponse;
import io.search.core.search.response.NaverBlogSearchResponse;
import io.search.core.search.response.SearchResponse;
import io.search.core.search.restclient.KakaoRestClient;
import io.search.core.search.restclient.NaverRestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final KakaoRestClient kakaoRestClient;
    private final NaverRestClient naverRestClient;

    public List<SearchResultDto> searchBlog(SearchForm searchForm, PagingForm pagingForm) {

        SearchResponse<?> searchResponse = kakaoRestClient.searchBlog(searchForm, pagingForm);

        if (searchResponse.getData() == null) {
            searchResponse = naverRestClient.searchBlog(searchForm, pagingForm);
            NaverBlogSearchResponse naverBlogSearchResponse = (NaverBlogSearchResponse) searchResponse.getData();
        }

        KakaoBlogSearchResponse kakaoBlogSearchResponse = (KakaoBlogSearchResponse) searchResponse.getData();

        List<SearchResultDto> searchResults = kakaoBlogSearchResponse.getDocuments()
                .stream()
                .map(SearchResultDto::from)
                .toList();

        return searchResults;
    }

}
