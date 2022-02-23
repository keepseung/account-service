package com.payservice.error.exception;

public class AccountDetailsNotExistException extends RuntimeException{
    public static final String errorCode = "account.details.not.exist";
    public AccountDetailsNotExistException(String message) {
        super(message);
    }

    public AccountDetailsNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
