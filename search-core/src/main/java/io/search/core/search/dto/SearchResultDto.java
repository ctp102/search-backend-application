package io.search.core.search.dto;

import io.search.core.search.response.KakaoBlogSearchResponse;
import lombok.Data;

// SearchResultDomain으로 넣어야 하나
@Data
public class SearchResultDto {

    private Long id;
    private String blogName;
    private String title;
    private String contents;
    private String blogUrl;
    private String postUrl;
    private String thumbnail;
    private java.sql.Timestamp createDt;

    public static SearchResultDto from(KakaoBlogSearchResponse.Document document) {
        SearchResultDto searchResultDto = new SearchResultDto();
        searchResultDto.setBlogName(document.getBlogname());
        searchResultDto.setTitle(document.getTitle());
        searchResultDto.setContents(document.getContents());
        searchResultDto.setThumbnail(document.getThumbnail());
        searchResultDto.setPostUrl(document.getUrl());
        searchResultDto.setCreateDt(document.getDatetime());
        return searchResultDto;
    }

}
