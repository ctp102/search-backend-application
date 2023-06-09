package io.search.core.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.search.core.config.properties.SearchProperties;
import io.search.core.config.properties.RestTemplateProperties;
import io.search.core.search.restclient.KakaoRestClient;
import io.search.core.search.restclient.NaverRestClient;
import io.search.core.search.restclient.RestClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
public class CoreConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateProperties restTemplateProperties) {

        // 1. ClientHttpRequestFactory requestFactory
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setDefaultMaxPerRoute(restTemplateProperties.getConnectionManager().getDefaultMaxPerRoute());
        connectionManager.setMaxTotal(restTemplateProperties.getConnectionManager().getMaxTotal());

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(HttpClientBuilder.create().setConnectionManager(connectionManager)
                .evictIdleConnections(restTemplateProperties.getClientHttpRequest().getEvictIdleConnections(), TimeUnit.SECONDS)
                .build());

        factory.setConnectTimeout(restTemplateProperties.getClientHttpRequest().getConnectTimeout());
        factory.setReadTimeout(restTemplateProperties.getClientHttpRequest().getReadTimeout());

        // 2. MessageConverters
        MappingJackson2HttpMessageConverter jsonHttpMessageConverter = new MappingJackson2HttpMessageConverter(objectMapper());
        jsonHttpMessageConverter.setSupportedMediaTypes(MediaType.parseMediaTypes("application/json"));
        jsonHttpMessageConverter.setPrefixJson(false);

        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter(StandardCharsets.UTF_8);
        stringConverter.setWriteAcceptCharset(false);

        ByteArrayHttpMessageConverter byteArrayHttpMessageConverter = new ByteArrayHttpMessageConverter();

        FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
        formHttpMessageConverter.setCharset(StandardCharsets.UTF_8);

        List<HttpMessageConverter<?>> httpMessageConverters = new ArrayList<>();
        httpMessageConverters.add(jsonHttpMessageConverter);
        httpMessageConverters.add(stringConverter);
        httpMessageConverters.add(byteArrayHttpMessageConverter);
        httpMessageConverters.add(formHttpMessageConverter);

        // 3. RestTemplate
        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setMessageConverters(httpMessageConverters);
        return restTemplate;
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return objectMapper;
    }

    @Bean(name = "kakaoRestClient")
    public RestClient kakaoRestClient(SearchProperties searchProperties, RestTemplate restTemplate, ObjectMapper objectMapper) {
        return new KakaoRestClient(searchProperties, restTemplate, objectMapper);
    }

    @Bean(name = "naverRestClient")
    public RestClient naverRestClient(SearchProperties searchProperties, RestTemplate restTemplate, ObjectMapper objectMapper) {
        return new NaverRestClient(searchProperties, restTemplate, objectMapper);
    }

}
