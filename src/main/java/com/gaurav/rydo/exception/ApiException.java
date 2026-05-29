package com.gaurav.rydo.exception;

public class ApiException extends RuntimeException {

    public ApiException(String message) {
        super(message);
    }
}