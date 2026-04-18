package com.mango.products.model;

import java.math.BigDecimal;

public class PriceValueResponse {

    private BigDecimal value;

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
