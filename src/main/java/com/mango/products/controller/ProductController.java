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

    @PostMapping("/{id}/prices")
    public ResponseEntity<PriceResponse> addProduct(
            @PathVariable("id") Long productId,
            @Valid @RequestBody PriceRequest priceRequest){
        PriceResponse priceResponse = productService.addPriceToProduct(productId, priceRequest);
        return new ResponseEntity<>(priceResponse, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/prices")
    public ResponseEntity<PriceValueResponse> getCurrentPrice(
            @PathVariable("id") Long productId,
            @RequestParam("date") LocalDate date) {
        PriceValueResponse priceValueResponse = productService.getCurrentPrice(productId, date);
        return new ResponseEntity<>(priceValueResponse, HttpStatus.OK);
    }
}
