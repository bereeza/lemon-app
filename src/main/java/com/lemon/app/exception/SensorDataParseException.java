package com.lemon.app.exception;

public class SensorDataParseException extends RuntimeException {
    public SensorDataParseException(String message) {
        super(message);
    }

    public SensorDataParseException(String message, Throwable cause) {
        super(message, cause);
    }
}
