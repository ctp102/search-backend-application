package io.search.core.search.response;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Data
public class NaverBlogSearchResponse {

    private java.sql.Timestamp lastBuildDate;
    private int total;
    private int start;
    private int display;
    private List<Item> items;

    @Getter
    @Setter
    public static class Item {
        private String title;
        private String link;
        private String description;

        @JsonAlias("bloggername")
        private String bloggerName;

        @JsonAlias("bloggerlink")
        private String bloggerLink;

        @JsonAlias("postdate")
        private java.sql.Timestamp postDate;
    }

}
