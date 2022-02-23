package com.payservice.error.exception;

public class DuplicateAccountException extends RuntimeException {
    public static final String errorCode = "account.duplicate";
    public DuplicateAccountException(String message) {
        super(message);
    }

    public DuplicateAccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
