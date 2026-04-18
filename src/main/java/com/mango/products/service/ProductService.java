package com.mango.products.service;

import com.mango.products.model.PriceRequest;
import com.mango.products.model.PriceResponse;
import com.mango.products.model.ProductRequest;
import com.mango.products.model.ProductResponse;

public interface ProductService {

    ProductResponse addProduct(ProductRequest productRequest);

    PriceResponse addPriceToProduct(Long productId, PriceRequest priceRequest);
}
