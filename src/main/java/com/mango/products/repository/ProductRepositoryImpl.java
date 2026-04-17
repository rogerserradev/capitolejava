package com.mango.products.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final JdbcTemplate jdbcTemplate;

    public ProductRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int addProduct(String productName, String productDescription) {
        String sql = "insert into product (name, description) values (?, ?)";
        return jdbcTemplate.update(sql, productName, productDescription);
    }
}
