package io.search.core.commons.exception;

public class CustomInvalidParameterException extends CustomException {

    public CustomInvalidParameterException(String message, int code) {
        super(message, code);
    }

    public CustomInvalidParameterException(String message, Throwable cause, int code) {
        super(message, cause, code);
    }

}
