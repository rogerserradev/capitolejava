package com.mango.products.exception;

import org.springframework.http.HttpStatus;

public enum ProductError {

    PRODUCT_ALREADY_EXISTS(
            "PRODUCT_ALREADY_EXISTS",
            "Product already exists",
            HttpStatus.CONFLICT
    );

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

    ProductError(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
