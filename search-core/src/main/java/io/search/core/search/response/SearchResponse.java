package io.search.core.search.response;

import lombok.Data;

@Data
public class SearchResponse<T> {

    private T data;
    private Error error;

    @Data
    public static class Error {
        private String errorType;
        private String message;
    }

}
