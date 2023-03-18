package io.search.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.search.core.config.properties.SearchProperties;
import io.search.core.search.KakaoBlogSearchResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
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

    public SearchResponse<KakaoBlogSearchResponse> getBlogSearch(Map<String, Object> paramMap) {

        URI uri = UriComponentsBuilder
                .fromUriString(endPoint)
                .path("/v2/search/blog")
                .queryParam("query", paramMap.get("query"))
                .queryParam("sort", paramMap.get("sort"))
                .queryParam("page", paramMap.get("page"))
                .queryParam("size", paramMap.get("size"))
                .encode()
                .build()
                .toUri();

        return get(HttpMethod.GET, uri, KakaoBlogSearchResponse.class);
    }

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

}
