package com.mango.products.repository.mapper;

import com.mango.products.model.PriceValueResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PriceValueMapper implements RowMapper<PriceValueResponse> {
    @Override
    public PriceValueResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        PriceValueResponse priceValueResponse = new PriceValueResponse();
        priceValueResponse.setValue(rs.getBigDecimal("value"));
        return priceValueResponse;
    }
}
