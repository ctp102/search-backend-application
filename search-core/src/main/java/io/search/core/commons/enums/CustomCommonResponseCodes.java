package io.search.core.commons.enums;

import lombok.Getter;

public enum CustomCommonResponseCodes implements CustomResponseCodes {

    SUCCESS         (200, "Success"),
    BAD_REQUEST     (400, "Invalid parameter"),
    UNAUTHORIZED    (401, "Authentication failed"),
    FORBIDDEN       (403, "Already exists"),
    NOT_FOUND       (404, "Unsupported"),
    ERROR           (500, "System Error")
    ;


    @Getter
    private final int number;

    @Getter
    private final String message;

    CustomCommonResponseCodes(int number, String message) {
        this.number = number;
        this.message = message;
    }

    public String getCode() {
        return this.name();
    }

}
