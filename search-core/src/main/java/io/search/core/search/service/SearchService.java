package io.search.core.search.service;

import io.search.core.commons.form.PagingForm;
import io.search.core.search.domain.SearchDomain;
import io.search.core.search.dto.SearchResultDto;
import io.search.core.search.enums.PlatformType;
import io.search.core.search.form.SearchForm;
import io.search.core.search.repository.SearchRepository;
import io.search.core.search.response.KakaoBlogSearchResponse;
import io.search.core.search.response.NaverBlogSearchResponse;
import io.search.core.search.response.SearchResponse;
import io.search.core.search.restclient.KakaoRestClient;
import io.search.core.search.restclient.NaverRestClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SearchService {

    private final SearchRepository searchRepository;
    private final KakaoRestClient kakaoRestClient;
    private final NaverRestClient naverRestClient;

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
     * @param query
     * @param platform
     */
    public void insertSearch(SearchDomain searchDomain, String query, PlatformType platform) {

        if (searchDomain == null) {
            searchRepository.save(new SearchDomain(query, platform));
        } else {
            searchDomain.increaseCount();
        }
    }

    /**
     * 블로그 검색
     *
     * @param searchForm
     * @param pagingForm
     * @return
     */
    public List<SearchResultDto> searchBlog(SearchForm searchForm, PagingForm pagingForm) {

        SearchResponse<?> searchResponse = kakaoRestClient.searchBlog(searchForm, pagingForm);

        // 카카오 검색 API에서 데이터를 불러오지 못하는 경우 네이버 검색 API 호출
        if (searchResponse == null || searchResponse.getData() == null) {
            searchResponse = naverRestClient.searchBlog(searchForm, pagingForm);
            NaverBlogSearchResponse naverBlogSearchResponse = (NaverBlogSearchResponse) searchResponse.getData();

            return getSearchResults(naverBlogSearchResponse);
        }

        KakaoBlogSearchResponse kakaoBlogSearchResponse = (KakaoBlogSearchResponse) searchResponse.getData();

        List<SearchResultDto> searchResults = getSearchResults(kakaoBlogSearchResponse);
        if (searchResults.isEmpty()) {
            return new ArrayList<>();
        }

        String query = searchForm.getQuery();
        PlatformType platform = PlatformType.KAKAO;
        
        SearchDomain searchDomain = getSearchByQueryAndPlatform(query, platform);
        
        insertSearch(searchDomain, query, platform);

        return searchResults;
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
        return null;
    }

}
