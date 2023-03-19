package io.search.core.search.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.search.core.commons.form.PagingForm;
import io.search.core.search.form.SearchForm;
import io.search.core.search.response.SearchResponse;
import io.search.core.search.response.kakao.KakaoBlogSearchResponse;
import io.search.core.search.restclient.kakao.KakaoRestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SearchService {

    private final KakaoRestClient kakaoRestClient;

    // kakaoRestClient, naverRestClient 모두 처리하기 위해 제네릭 메서드로 처리
    public <T> SearchResponse<T> searchBlog(SearchForm searchForm, PagingForm pagingForm) {

        SearchResponse<KakaoBlogSearchResponse> response = kakaoRestClient.searchBlog(searchForm, pagingForm);

        // TODO: [2023-03-18] error 있으면 naver api로 조회
        // SearchResponse<NaverBlogSearchResponse> response = naverRestClient.searchBlog(searchForm, pagingForm);

        return (SearchResponse<T>) response;
    }

}
