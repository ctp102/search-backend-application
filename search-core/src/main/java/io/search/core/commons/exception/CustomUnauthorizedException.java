package io.search.core.commons.exception;

public class CustomUnauthorizedException extends CustomException {

    public CustomUnauthorizedException(String message, int code) {
        super(message, code);
    }

    public CustomUnauthorizedException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

}
