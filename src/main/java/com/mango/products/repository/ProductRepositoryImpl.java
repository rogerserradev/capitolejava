package com.mango.products.repository;

import com.mango.products.model.ProductResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addProduct(String productName, String productDescription) {
        String insert = "INSERT INTO product (name, description) VALUES (?, ?)";
        jdbcTemplate.update(insert, productName, productDescription);
    }

    @Override
    public boolean productAlreadyExists(String productName, String productDescription) {
        // exists is more optimal than count (*)
        String select = "SELECT EXISTS (SELECT 1 FROM product WHERE name = ? AND description = ? )";
        return jdbcTemplate.queryForObject(select, Boolean.class, productName, productDescription);
    }


}
