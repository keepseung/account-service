package com.payservice.error.exception;

public class UserNotExistException extends RuntimeException {
    public static final String errorCode = "user.not.exist";

    public UserNotExistException(String message) {
        super(message);
    }

    public UserNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
