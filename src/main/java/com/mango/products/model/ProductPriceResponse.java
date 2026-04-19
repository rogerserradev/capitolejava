package com.mango.products.model;

import java.util.List;

public class ProductPriceResponse {

    private String name;
    private String description;
    private List<PriceResponse> prices;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<PriceResponse> getPrices() {
        return prices;
    }

    public void setPrices(List<PriceResponse> prices) {
        this.prices = prices;
    }

}
