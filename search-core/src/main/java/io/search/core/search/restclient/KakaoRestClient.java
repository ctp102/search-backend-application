package io.search.core.search.restclient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.search.core.commons.form.PagingForm;
import io.search.core.config.properties.SearchProperties;
import io.search.core.search.form.SearchForm;
import io.search.core.search.response.SearchResponse;
import io.search.core.search.response.KakaoBlogSearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
public class KakaoRestClient implements RestClient {

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
    @SuppressWarnings("unchecked")
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

        SearchResponse<KakaoBlogSearchResponse> searchResponse = new SearchResponse<>();
        SearchResponse<KakaoBlogSearchResponse> tempResponse = get(HttpMethod.GET, uri);

        if (tempResponse.getData() != null) {
            KakaoBlogSearchResponse kakaoBlogSearchResponse = objectMapper.convertValue(tempResponse.getData(), KakaoBlogSearchResponse.class);
            pagingForm.setTotalCount(kakaoBlogSearchResponse.getMeta().getTotalCount());
            searchResponse.setData(kakaoBlogSearchResponse);
        }

        return searchResponse;
    }

    public <T> SearchResponse<T> get(HttpMethod httpMethod, URI uri) {
        SearchResponse<T> response = new SearchResponse<>();

        HttpHeaders httpHeaders = getHttpHeaders(apiKey);
        RequestEntity<?> requestEntity = new RequestEntity<>(httpHeaders, httpMethod, uri);

        log.info("[GET] REST CALL: {}", uri);

        try {
            ResponseEntity<T> responseEntity = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {});
            response.setData(responseEntity.getBody());
        } catch (HttpStatusCodeException e) {
            log.error("[HttpStatusCodeException]: {} ", e.getMessage());
            try {
                response.setError(objectMapper.readValue(e.getResponseBodyAsString(), SearchResponse.Error.class));
            } catch (JsonProcessingException ex) {
                throw new RuntimeException(ex);
            }
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

}
