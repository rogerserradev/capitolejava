package com.mango.products.controller;

import com.mango.products.model.*;
import com.mango.products.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @PostMapping("/{productId}/prices")
    public ResponseEntity<PriceResponse> addPrice(
            @PathVariable("productId") Long productId,
            @Valid @RequestBody PriceRequest priceRequest){
        PriceResponse priceResponse = productService.addPriceToProduct(productId, priceRequest);
        return new ResponseEntity<>(priceResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{productId}/prices")
    public ResponseEntity<PriceValueResponse> getCurrentPrice(
            @PathVariable("productId") Long productId,
            @RequestParam("date") LocalDate date) {
        PriceValueResponse priceValueResponse = productService.getCurrentPrice(productId, date);
        return new ResponseEntity<>(priceValueResponse, HttpStatus.OK);
    }

    @GetMapping("/{productId}/prices/history") // Ambiguous mapping if I keep the route /{id}/prices
    public ResponseEntity<ProductPriceResponse> getProductPriceHistory(
            @PathVariable("productId") Long productId) {
        ProductPriceResponse productPriceResponse = productService.getProductPriceHistory(productId);
        return new ResponseEntity<>(productPriceResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}/prices/{priceId}")
    public ResponseEntity<Void> deletePrice(
            @PathVariable("productId") Long productId,
            @PathVariable("priceId") Long priceId) {
        productService.deletePrice(productId, priceId);
        return ResponseEntity.noContent().build();
    }

    // thought about @Patch, but value and initDate can't be null
    @PutMapping("/{productId}/prices/{priceId}")
    public ResponseEntity<PriceResponse> updatePrice(
            @PathVariable("productId") Long productId,
            @PathVariable("priceId") Long priceId,
            @Valid @RequestBody PriceRequest priceRequest
    ) {
        PriceResponse priceResponse = productService.updatePrice(productId, priceId, priceRequest);
        return new ResponseEntity<>(priceResponse, HttpStatus.OK);
    }


}
