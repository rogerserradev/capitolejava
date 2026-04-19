package com.mango.products.exception;

import org.springframework.http.HttpStatus;

public class ProductServiceException extends RuntimeException {

    private String errorCode;
    private HttpStatus httpStatus;

    public ProductServiceException(String message, String errorCode, HttpStatus httpStatus){
        super(message);
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
