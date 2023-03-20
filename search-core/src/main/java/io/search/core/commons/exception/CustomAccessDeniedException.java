package io.search.core.commons.exception;

public class CustomAccessDeniedException extends CustomException {

    public CustomAccessDeniedException(String message, int code) {
        super(message, code);
    }

    public CustomAccessDeniedException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

}
