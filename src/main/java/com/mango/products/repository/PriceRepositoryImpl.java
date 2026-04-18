package com.mango.products.repository;

import com.mango.products.model.PriceResponse;
import com.mango.products.model.PriceValueResponse;
import com.mango.products.repository.mapper.PriceValueMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@Repository
public class PriceRepositoryImpl implements PriceRepository{

    private final JdbcTemplate jdbcTemplate;

    public PriceRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean priceExistsBetweenInitDateAndEndDate(Long productId, LocalDate initDate, LocalDate endDate) {
        if (endDate != null) {
            String select = "SELECT EXISTS (SELECT 1 FROM price WHERE product_id = ? AND init_date <= ? AND (end_date IS NULL OR end_date >= ?))";
            return jdbcTemplate.queryForObject(select, Boolean.class, productId, endDate, initDate);
        } else {
            String select = "SELECT EXISTS (SELECT 1 FROM price WHERE product_id = ? AND (end_date IS NULL OR end_date >= ?))";
            return jdbcTemplate.queryForObject(select, Boolean.class, productId, initDate);
        }
    }

    @Override
    public void addPrice(Long productId, BigDecimal value, LocalDate initDate, LocalDate endDate) {
        String insert = "INSERT INTO price (product_id, value, init_date, end_date) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(insert, productId, value, initDate, endDate);
    }

    @Override
    public Optional<PriceValueResponse> getCurrentPrice(Long productId, LocalDate date) {
        String select = "SELECT value FROM price WHERE product_id = ? AND init_date <= ? AND (end_date IS NULL OR end_date >= ?)";
        PriceValueResponse priceValueResponse = jdbcTemplate.queryForObject(select, new PriceValueMapper(), productId, date, date);
        return Optional.ofNullable(priceValueResponse);
    }

}
