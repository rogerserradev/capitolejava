package com.mango.products.controller;

import com.mango.products.model.PriceRequest;
import com.mango.products.model.PriceResponse;
import com.mango.products.model.ProductRequest;
import com.mango.products.model.ProductResponse;
import com.mango.products.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping()
    public ResponseEntity<ProductResponse> addProduct(@RequestBody ProductRequest productRequest){
        ProductResponse productResponse = productService.addProduct(productRequest);
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

    @PostMapping("/{id}/prices")
    public ResponseEntity<PriceResponse> addProduct(
            @PathVariable("id") Long productId,
            @Valid @RequestBody PriceRequest priceRequest){
        PriceResponse priceResponse = productService.addPriceToProduct(productId, priceRequest);
        return new ResponseEntity<>(priceResponse, HttpStatus.CREATED);
    }
}
