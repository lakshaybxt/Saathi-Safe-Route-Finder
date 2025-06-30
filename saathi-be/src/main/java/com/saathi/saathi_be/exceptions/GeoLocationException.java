package com.saathi.saathi_be.exceptions;

public class GeoLocationException extends BaseException {
    public GeoLocationException() {
    }

    public GeoLocationException(String message) {
        super(message);
    }

    public GeoLocationException(String message, Throwable cause) {
        super(message, cause);
    }

    public GeoLocationException(Throwable cause) {
        super(cause);
    }
}
