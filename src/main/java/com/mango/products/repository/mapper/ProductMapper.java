package com.mango.products.repository.mapper;

import com.mango.products.model.ProductResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

// https://www.baeldung.com/spring-jdbc-jdbctemplate
public class ProductMapper implements RowMapper<ProductResponse> {
    @Override
    public ProductResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setName(rs.getString("name"));
        productResponse.setDescription(rs.getString("description"));
        return productResponse;
    }
}
