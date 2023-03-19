package io.search.core.search.response.kakao;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Document {

    private String title;
    private String contents;
    private String url;
    private String blogname;
    private String thumbnail;
    private java.sql.Timestamp datetime;

}
