package com.github.maximtereshchenko.clerk.write.api.exception;

public final class UnexpectedException extends RuntimeException {

    public UnexpectedException(String message, Throwable cause) {
        super(message, cause);
    }
}
