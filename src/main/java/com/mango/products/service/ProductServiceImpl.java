package com.mango.products.service;

import com.mango.products.converter.ProductConverter;
import com.mango.products.exception.ProductError;
import com.mango.products.exception.ProductServiceException;
import com.mango.products.model.ProductRequest;
import com.mango.products.model.ProductResponse;
import com.mango.products.repository.ProductRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductResponse addProduct(ProductRequest productRequest) {
        validateRequest(productRequest);
        productRepository.addProduct(productRequest.getName(), productRequest.getDescription());
        logger.info("Product created");
        return ProductConverter.fromRequestToResponse(productRequest);
    }

    private void validateRequest(ProductRequest productRequest) {
        boolean productAlreadyExists = productRepository.productAlreadyExists(productRequest.getName(), productRequest.getDescription());
        if (productAlreadyExists) { // throw conflict exception
            throw new ProductServiceException(ProductError.PRODUCT_ALREADY_EXISTS.getMessage(), ProductError.PRODUCT_ALREADY_EXISTS.getCode(), ProductError.PRODUCT_ALREADY_EXISTS.getHttpStatus());
        }
    }
}
