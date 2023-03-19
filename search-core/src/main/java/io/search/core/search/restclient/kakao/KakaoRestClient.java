package io.search.core.search.restclient.kakao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.search.core.commons.form.PagingForm;
import io.search.core.config.properties.SearchProperties;
import io.search.core.search.form.SearchForm;
import io.search.core.search.response.kakao.KakaoBlogSearchResponse;
import io.search.core.search.response.SearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class KakaoRestClient {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String endPoint;
    private final String apiKey;

    public KakaoRestClient(SearchProperties searchProperties, RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.endPoint       = searchProperties.getKakao().getEndPoint();
        this.apiKey         = searchProperties.getKakao().getApiKey();
        this.restTemplate   = restTemplate;
        this.objectMapper   = objectMapper;
    }

    /**
     * 블로그 검색
     * @param searchForm
     * @param pagingForm
     * @return
     */
    public SearchResponse<KakaoBlogSearchResponse> searchBlog(SearchForm searchForm, PagingForm pagingForm) {

        URI uri = UriComponentsBuilder
                .fromUriString(endPoint)
                .path("/v2/search/blog")
                .queryParam("query", searchForm.getQuery())
                .queryParam("sort", searchForm.getSort())
                .queryParam("page", pagingForm.getPage())
                .queryParam("size", pagingForm.getSize())
                .encode()
                .build()
                .toUri();

        return get(HttpMethod.GET, uri, KakaoBlogSearchResponse.class);
    }

//    /**
//     * 블로그 검색
//     * @param searchForm
//     * @param pagingForm
//     * @return
//     */
//    public SearchResponse<KakaoBlogSearchResponse> searchBlog(SearchForm searchForm, PagingForm pagingForm) {
//
//        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
//                .fromUriString(endPoint)
//                .path("/v2/search/blog")
//                .encode();
//
//        Map<String, Object> paramMap = setParamMap(searchForm, pagingForm);
//        setQueryParam(uriComponentsBuilder, paramMap);
//
//        URI uri = uriComponentsBuilder.build().toUri();
//
//        return get(HttpMethod.GET, uri, KakaoBlogSearchResponse.class);
//    }
//
//    private Map<String, Object> setParamMap(SearchForm searchForm, PagingForm pagingForm) {
//        Map<String, Object> paramMap = new HashMap<>();
//        paramMap.put("query", searchForm.getQuery());
//        paramMap.put("sort",  searchForm.getSort());
//        paramMap.put("page",  pagingForm.getPage());
//        paramMap.put("size",  pagingForm.getSize());
//        return paramMap;
//    }

    public <T> SearchResponse<T> get(HttpMethod httpMethod, URI uri, Class<T> toValueType) {
        SearchResponse<T> response = new SearchResponse<>();

        HttpHeaders httpHeaders = getHttpHeaders(apiKey);
        RequestEntity<?> requestEntity = new RequestEntity<>(httpHeaders, httpMethod, uri);

        log.info("[GET] REST CALL: {}", uri);

        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {});
            response.setData(responseEntity.getBody());
        } catch (HttpStatusCodeException e) {
            log.error("[HttpStatusCodeException]: {} ", e.getMessage());
        } catch (Exception e) {
            log.error("[Exception] ", e);
        }
        return response;
    }

    public HttpHeaders getHttpHeaders(String apiKey) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "KakaoAK " + apiKey);
        return httpHeaders;
    }

    private void setQueryParam(UriComponentsBuilder builder, Map<String, Object> paramMap) {
        for (String key : paramMap.keySet()) {
            builder.queryParam(key, paramMap.get(key));
        }
    }

}
