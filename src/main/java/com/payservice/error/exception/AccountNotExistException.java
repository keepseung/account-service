package com.payservice.error.exception;

public class AccountNotExistException extends RuntimeException{
    public static final String errorCode = "account.not.exist";
    public AccountNotExistException(String message) {
        super(message);
    }

    public AccountNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
