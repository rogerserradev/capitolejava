package com.mango.products.converter;

import com.mango.products.model.PriceRequest;
import com.mango.products.model.PriceResponse;
import com.mango.products.model.ProductRequest;
import com.mango.products.model.ProductResponse;

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

}
