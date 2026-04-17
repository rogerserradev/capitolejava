package com.mango.products.service;

import com.mango.products.converter.ProductConverter;
import com.mango.products.model.ProductRequest;
import com.mango.products.model.ProductResponse;
import com.mango.products.repository.ProductRepository;
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
        productRepository.addProduct(productRequest.getName(), productRequest.getDescription());
        logger.info("Product created");
        return ProductConverter.fromRequestToResponse(productRequest);
    }
}
