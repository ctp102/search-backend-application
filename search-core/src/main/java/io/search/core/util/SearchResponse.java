package io.search.core.util;

import lombok.Data;

@Data
public class SearchResponse<T> {

    private T data;

}
