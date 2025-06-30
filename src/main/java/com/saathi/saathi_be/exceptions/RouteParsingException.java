package com.saathi.saathi_be.exceptions;

public class RouteParsingException extends BaseException {
    public RouteParsingException() {
    }

    public RouteParsingException(String message) {
        super(message);
    }

    public RouteParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RouteParsingException(Throwable cause) {
        super(cause);
    }
}
