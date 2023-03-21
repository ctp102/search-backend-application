package io.search.core.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("search.blog")
public class SearchProperties {

    private Kakao kakao;
    private Naver naver;

    @Getter
    @Setter
    public static class Kakao {
        private String endPoint;
        private String apiKey;
    }

    @Getter
    @Setter
    public static class Naver {
        private String endPoint;
        private String clientId;
        private String clientSecret;
    }

}
