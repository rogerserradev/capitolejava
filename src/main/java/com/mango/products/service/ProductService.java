package com.mango.products.service;

import com.mango.products.model.*;
import jakarta.validation.Valid;

import java.time.LocalDate;

public interface ProductService {

    ProductResponse addProduct(ProductRequest productRequest);

    PriceResponse addPriceToProduct(Long productId, PriceRequest priceRequest);

    PriceValueResponse getCurrentPrice(Long productId, LocalDate date);

    ProductPriceResponse getProductPriceHistory(Long productId);

    void deletePrice(Long productId, Long priceId);

    PriceResponse updatePrice(Long productId, Long priceId, PriceRequest priceRequest);
}
