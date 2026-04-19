package com.mango.products.converter;

import com.mango.products.model.*;

import java.util.List;

public class ProductConverter {

    public static ProductResponse fromRequestToResponse(ProductRequest productRequest) {
        ProductResponse productResponse = new ProductResponse();
        if (productRequest != null) {
            productResponse.setName(productRequest.getName());
            productResponse.setDescription(productRequest.getDescription());
        }
        return productResponse;
    }

    public static PriceResponse fromRequestToResponse(PriceRequest priceRequest) {
        PriceResponse priceResponse = new PriceResponse();
        priceResponse.setValue(priceRequest.getValue());
        priceResponse.setInitDate(priceRequest.getInitDate());
        priceResponse.setEndDate(priceRequest.getEndDate());
        return priceResponse;
    }

    public static ProductPriceResponse fromRequestToResponse(ProductResponse productResponse, List<PriceResponse> priceHistoryList) {
        ProductPriceResponse productPriceResponse = new ProductPriceResponse();
        productPriceResponse.setName(productResponse.getName());
        productPriceResponse.setDescription(productResponse.getDescription());
        productPriceResponse.setPrices(priceHistoryList);
        return productPriceResponse;
    }
}
