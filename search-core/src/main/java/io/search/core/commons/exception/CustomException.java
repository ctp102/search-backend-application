package io.search.core.commons.exception;

public class CustomException extends RuntimeException {

    protected int code;

    public CustomException(String message, int code) {
        super(message);
        this.code = code;
    }

    public CustomException(String message, Throwable cause, int code) {
        super(message, cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
