package com.mango.products.repository;

import com.mango.products.model.ProductResponse;

import java.util.Optional;

public interface ProductRepository {

    void addProduct(String productName, String productDescription);
    boolean productAlreadyExists(String productName, String productDescription);
    Optional<ProductResponse> findProductById(Long productId);

}
