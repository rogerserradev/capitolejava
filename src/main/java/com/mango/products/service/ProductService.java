package com.mango.products.service;

import com.mango.products.model.*;

import java.time.LocalDate;

public interface ProductService {

    ProductResponse addProduct(ProductRequest productRequest);

    PriceResponse addPriceToProduct(Long productId, PriceRequest priceRequest);

    PriceValueResponse getCurrentPrice(Long productId, LocalDate date);
}
