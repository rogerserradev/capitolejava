package com.mango.products.repository;

public interface ProductRepository {

    void addProduct(String productName, String productDescription);
    boolean productAlreadyExists(String productName, String productDescription);

}
