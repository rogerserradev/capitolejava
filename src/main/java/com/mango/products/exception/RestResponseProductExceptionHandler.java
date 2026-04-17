package com.mango.products.exception;

import com.mango.products.model.ProductErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseProductExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProductServiceException.class)
    public ResponseEntity<ProductErrorResponse> handleProductServiceException(ProductServiceException exception){
        ProductErrorResponse errorResponse = new ProductErrorResponse();
        errorResponse.setErrorMessage(exception.getMessage());
        errorResponse.setErrorCode(exception.getErrorCode());
        return new ResponseEntity<>(errorResponse, exception.getHttpStatus());
    }
}
