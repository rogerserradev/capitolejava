package com.mango.products.exception;

import org.springframework.http.HttpStatus;

public enum ProductError {

    PRODUCT_ALREADY_EXISTS(
            "PRODUCT_ALREADY_EXISTS",
            "Product already exists",
            HttpStatus.CONFLICT
    ),
    PRODUCT_NOT_FOUND(
            "PRODUCT_NOT_FOUND",
            "Product not found with given id",
            HttpStatus.NOT_FOUND
    ),
    INIT_DATE_IS_NULL(
            "INIT_DATE_IS_NULL",
            "Init date can't be null",
            HttpStatus.BAD_REQUEST
    ),
    INVALID_END_DATE(
            "INVALID_END_DATE",
            "Invalid end date",
            HttpStatus.BAD_REQUEST
    ),
    PRICE_ALREADY_EXISTS(
            "PRICE_ALREADY_EXISTS",
            "Price already exists between init date and end date",
            HttpStatus.CONFLICT
    ),
    PRICE_NOT_FOUND(
            "PRICE_NOT_FOUND",
            "Price not found",
            HttpStatus.NOT_FOUND
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
