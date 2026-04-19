package com.mango.products.repository;

import com.mango.products.model.PriceResponse;
import com.mango.products.model.PriceValueResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PriceRepository {

    boolean priceExistsBetweenInitDateAndEndDate(Long productId, LocalDate initDate, LocalDate endDate);

    void addPrice(Long productId, BigDecimal value, LocalDate initDate, LocalDate endDate);

    Optional<PriceValueResponse> getCurrentPrice(Long productId, LocalDate date);

    List<PriceResponse> getHistoryPricesFromProduct(Long productId);

    void deletePrice(Long productId, Long priceId);

    void updatePrice(Long productId, Long priceId, BigDecimal value, LocalDate initDate, LocalDate endDate);
}
