package io.search.core.search.response.kakao;

import lombok.Data;

import java.util.List;

@Data
public class KakaoBlogSearchResponse {

    private Meta meta;
    private List<Document> documents;

}
