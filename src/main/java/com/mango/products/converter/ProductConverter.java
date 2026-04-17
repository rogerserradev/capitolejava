package com.mango.products.converter;

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

}
