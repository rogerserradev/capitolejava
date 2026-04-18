package com.mango.products.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PriceRepository {

    boolean priceExistsBetweenInitDateAndEndDate(Long productId, LocalDate initDate, LocalDate endDate);

    void addPrice(Long productId, BigDecimal value, LocalDate initDate, LocalDate endDate);
}
