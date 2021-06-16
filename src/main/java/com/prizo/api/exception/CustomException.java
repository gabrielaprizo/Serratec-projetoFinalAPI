package com.prizo.api.exception;

@SuppressWarnings("serial")
public class CustomException extends Exception {
    public CustomException(String message) {
        super(message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return this;
    }
}
