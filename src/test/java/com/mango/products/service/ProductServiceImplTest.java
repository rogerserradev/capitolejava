package com.mango.products.service;

import com.mango.products.exception.ProductError;
import com.mango.products.exception.ProductServiceException;
import com.mango.products.model.ProductRequest;
import com.mango.products.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductRequest productRequest;

    @BeforeEach
    void setup() {
        productRequest = new ProductRequest();
        productRequest.setName("Tank Top");
        productRequest.setDescription("Designed to keep you comfortable and stylish during your sweatiest workouts");
    }

    @Test
    void addProductShouldNotThrowException(){

    }

    @Test
    void addProductShouldThrowAlreadyExistsException(){
        when(productRepository.productAlreadyExists(productRequest.getName(), productRequest.getDescription())).thenReturn(true);
        ProductServiceException exception = assertThrows(ProductServiceException.class, () -> productService.addProduct(productRequest));
        assertEquals(ProductError.PRODUCT_ALREADY_EXISTS.getMessage(), exception.getMessage());
        assertEquals(ProductError.PRODUCT_ALREADY_EXISTS.getCode(), exception.getErrorCode());
        assertEquals(ProductError.PRODUCT_ALREADY_EXISTS.getHttpStatus(), exception.getHttpStatus()); // Conflict
    }
}
