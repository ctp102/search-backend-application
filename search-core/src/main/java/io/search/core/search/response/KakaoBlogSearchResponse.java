package io.search.core.search.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class KakaoBlogSearchResponse {

    private Meta meta;
    private List<Document> documents;

    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Meta {
        private int totalCount;
        private int pageableCount;
        private boolean isEnd;
    }

    @Getter
    @Setter
    public static class Document {
        private String title;
        private String contents;
        private String url;
        private String blogname;
        private String thumbnail;
        private java.sql.Timestamp datetime;
    }

}
