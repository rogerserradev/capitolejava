package com.mango.products.repository.mapper;

import com.mango.products.model.PriceResponse;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class PriceMapper implements RowMapper<PriceResponse> {
    @Override
    public PriceResponse mapRow(ResultSet rs, int rowNum) throws SQLException {
        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setValue(rs.getBigDecimal("value"));
        priceResponse.setInitDate(
                Optional.ofNullable(rs.getDate("init_date"))
                        .map(Date::toLocalDate)
                        .orElse(null)
        );

        priceResponse.setEndDate(
                Optional.ofNullable(rs.getDate("end_date"))
                        .map(Date::toLocalDate)
                        .orElse(null)
        );
        return priceResponse;
    }
}
