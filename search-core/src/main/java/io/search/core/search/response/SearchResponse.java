package io.search.core.search.response;

import lombok.Data;

@Data
public class SearchResponse<T> {

    private T data;

}
