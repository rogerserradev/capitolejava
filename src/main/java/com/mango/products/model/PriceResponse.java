package com.mango.products.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PriceResponse {

    private BigDecimal value;

    private LocalDate initDate;

    private LocalDate endDate;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LocalDate getInitDate() {
        return initDate;
    }

    public void setInitDate(LocalDate initDate) {
        this.initDate = initDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
