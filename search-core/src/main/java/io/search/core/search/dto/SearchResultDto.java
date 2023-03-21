package io.search.core.search.dto;

import io.search.core.search.response.KakaoBlogSearchResponse;
import io.search.core.search.response.NaverBlogSearchResponse;
import lombok.Data;

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

    public static SearchResultDto from(NaverBlogSearchResponse.Item item) {
        SearchResultDto searchResultDto = new SearchResultDto();
        searchResultDto.setBlogName(item.getBloggerName());
        searchResultDto.setTitle(item.getTitle());
        searchResultDto.setContents(item.getDescription());
        searchResultDto.setThumbnail(null);
        searchResultDto.setPostUrl(item.getLink());
        searchResultDto.setCreateDt(item.getPostDate());
        return searchResultDto;
    }

}
