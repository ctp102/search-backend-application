package io.search.core.search.service;

import io.search.core.commons.enums.CustomCommonResponseCodes;
import io.search.core.commons.exception.CustomException;
import io.search.core.commons.form.PagingForm;
import io.search.core.search.domain.SearchDomain;
import io.search.core.search.dto.SearchResultDto;
import io.search.core.search.enums.PlatformType;
import io.search.core.search.form.SearchForm;
import io.search.core.search.repository.SearchRepository;
import io.search.core.search.response.KakaoBlogSearchResponse;
import io.search.core.search.response.NaverBlogSearchResponse;
import io.search.core.search.response.SearchResponse;
import io.search.core.search.restclient.RestClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@Transactional
public class SearchService {

    private final SearchRepository searchRepository;
    private final RestClient kakaoRestClient;
    private final RestClient naverRestClient;

    public SearchService(SearchRepository searchRepository, @Qualifier("kakaoRestClient") RestClient kakaoRestClient, @Qualifier("naverRestClient") RestClient naverRestClient) {
        this.searchRepository = searchRepository;
        this.kakaoRestClient = kakaoRestClient;
        this.naverRestClient = naverRestClient;
    }

    /**
     * 인기 검색어 조회
     *
     * @return
     */
    public List<SearchDomain> getHotTop10Search() {
        return searchRepository.findTop10HotSearchByOrderByCountDesc();
    }

    /**
     * 검색어 단 건 조회(by query, platform)
     *
     * @param query
     * @param platform
     * @return
     */
    public SearchDomain getSearchByQueryAndPlatform(String query, PlatformType platform) {
        return searchRepository.findSearchByQueryAndPlatform(query, platform);
    }

    /**
     * 검색어 저장
     *
     * @param searchDomain
     */
    public void insertSearch(SearchDomain searchDomain) {
        searchRepository.save(searchDomain);
    }

    /**
     * 블로그 검색
     *
     * @param searchForm
     * @param pagingForm
     * @return
     */
    public List<SearchResultDto> searchBlog(SearchForm searchForm, PagingForm pagingForm) {

        String query = searchForm.getQuery();
        PlatformType platform = PlatformType.KAKAO;

        SearchResponse<?> searchResponse = kakaoRestClient.searchBlog(searchForm, pagingForm);

        // 카카오 검색 API에서 데이터를 불러오지 못하는 경우 네이버 검색 API 호출
        if (searchResponse == null || searchResponse.getData() == null) {
            platform = PlatformType.NAVER;

            searchForm.changeSortType(platform);
            searchResponse = naverRestClient.searchBlog(searchForm, pagingForm);
        }

        List<SearchResultDto> searchResults = getSearchResults(searchResponse.getData());
        if (searchResults.isEmpty()) {
            return new ArrayList<>();
        }

        SearchDomain searchDomain = getSearchByQueryAndPlatform(query, platform);

        if (searchDomain == null) {
            insertSearch(new SearchDomain(query, platform));
        } else {
            searchDomain.increaseCount();
        }

        return searchResults;
    }

    /**
     * 검색 API 결과를 SearchResultDto 리스트로 변환
     *
     * @param data
     * @return
     */
    private List<SearchResultDto> getSearchResults(Object data) {

        if (data instanceof KakaoBlogSearchResponse) {
            return getSearchResults((KakaoBlogSearchResponse) data);
        } else if (data instanceof NaverBlogSearchResponse) {
            return getSearchResults((NaverBlogSearchResponse) data);
        } else {
            throw new CustomException("지원하지 않는 검색 API 플랫폼입니다.", CustomCommonResponseCodes.ERROR.getNumber());
        }
    }

    /**
     * 카카오 검색 API 결과를 SearchResultDto 리스트로 변환
     *
     * @param searchResponse
     * @return
     */
    private List<SearchResultDto> getSearchResults(KakaoBlogSearchResponse searchResponse) {
        return searchResponse.getDocuments()
                .stream()
                .map(SearchResultDto::from)
                .toList();
    }

    /** 
     * 네이버 검색 API 결과를 SearchResultDto 리스트로 변환
     *
     * @param searchResponse
     * @return
     */
    private List<SearchResultDto> getSearchResults(NaverBlogSearchResponse searchResponse) {
        return searchResponse.getItems()
                .stream()
                .map(SearchResultDto::from)
                .toList();
    }

}
