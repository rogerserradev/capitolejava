package com.mango.products.service;

import com.mango.products.exception.ProductError;
import com.mango.products.exception.ProductServiceException;
import com.mango.products.model.*;
import com.mango.products.repository.PriceRepository;
import com.mango.products.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PriceRepository priceRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void addProductShouldNotThrowException(){
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Tank Top");
        productRequest.setDescription("Designed to keep you comfortable and stylish during your sweatiest workouts");

        when(productRepository.productAlreadyExists(productRequest.getName(), productRequest.getDescription())).thenReturn(false);
        ProductResponse response = productService.addProduct(productRequest);
        verify(productRepository, times(1)).addProduct(productRequest.getName(), productRequest.getDescription());
        assertNotNull(response);
        assertEquals(response.getName(), productRequest.getName());
        assertEquals(response.getDescription(), productRequest.getDescription());
    }

    @Test
    void addProductShouldThrowAlreadyExistsException(){
        ProductRequest productRequest = new ProductRequest();
        productRequest.setName("Tank Top");
        productRequest.setDescription("Designed to keep you comfortable and stylish during your sweatiest workouts");

        when(productRepository.productAlreadyExists(productRequest.getName(), productRequest.getDescription())).thenReturn(true);
        ProductServiceException exception = assertThrows(ProductServiceException.class, () -> productService.addProduct(productRequest));
        assertEquals(ProductError.PRODUCT_ALREADY_EXISTS.getMessage(), exception.getMessage());
        assertEquals(ProductError.PRODUCT_ALREADY_EXISTS.getCode(), exception.getErrorCode());
        assertEquals(ProductError.PRODUCT_ALREADY_EXISTS.getHttpStatus(), exception.getHttpStatus()); // Conflict
    }

    @Test
    void shouldThrowExceptionWhenInitDateIsNull(){
        Long productId = 1L;
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setValue(new BigDecimal("20.99"));

        ProductServiceException exception = assertThrows(ProductServiceException.class, () -> productService.addPriceToProduct(productId, priceRequest));
        assertEquals(ProductError.INIT_DATE_IS_NULL.getMessage(), exception.getMessage());
        assertEquals(ProductError.INIT_DATE_IS_NULL.getCode(), exception.getErrorCode());
        assertEquals(ProductError.INIT_DATE_IS_NULL.getHttpStatus(), exception.getHttpStatus()); // Conflict

    }

    @Test
    void shouldThrowExceptionWhenEndDateIsAfterInitDate(){
        Long productId = 1L;
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setValue(new BigDecimal("20.99"));
        priceRequest.setInitDate(LocalDate.now());
        priceRequest.setEndDate(LocalDate.of(2020, 5, 5));

        ProductServiceException exception = assertThrows(ProductServiceException.class, () -> productService.addPriceToProduct(productId, priceRequest));
        assertEquals(ProductError.INVALID_END_DATE.getMessage(), exception.getMessage());
        assertEquals(ProductError.INVALID_END_DATE.getCode(), exception.getErrorCode());
        assertEquals(ProductError.INVALID_END_DATE.getHttpStatus(), exception.getHttpStatus()); // Conflict

    }

    @Test
    void shouldThrowExceptionWhenWeAddPriceToInexistentProduct(){
        Long productId = 1L;
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setValue(new BigDecimal("20.99"));
        priceRequest.setInitDate(LocalDate.of(2020, 1, 1));
        priceRequest.setEndDate(LocalDate.of(2020, 5, 5));

        when(productRepository.findProductById(productId)).thenReturn(Optional.empty());
        ProductServiceException exception = assertThrows(ProductServiceException.class, () -> productService.addPriceToProduct(productId, priceRequest));
        assertEquals(ProductError.PRODUCT_NOT_FOUND.getMessage(), exception.getMessage());
        assertEquals(ProductError.PRODUCT_NOT_FOUND.getCode(), exception.getErrorCode());
        assertEquals(ProductError.PRODUCT_NOT_FOUND.getHttpStatus(), exception.getHttpStatus()); // Conflict

    }

    @Test
    void shouldThrowExceptionWhenPriceAlreadyExistsBetweenInitDateAndEndDate(){
        Long productId = 1L;
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setValue(new BigDecimal("20.99"));
        priceRequest.setInitDate(LocalDate.of(2020, 1, 1));
        priceRequest.setEndDate(LocalDate.of(2020, 5, 5));

        ProductResponse productResponse = mock(ProductResponse.class);
        when(productRepository.findProductById(productId)).thenReturn(Optional.of(productResponse));
        when(priceRepository.priceExistsBetweenInitDateAndEndDate(productId, priceRequest.getInitDate(), priceRequest.getEndDate())).thenReturn(true);
        ProductServiceException exception = assertThrows(ProductServiceException.class, () -> productService.addPriceToProduct(productId, priceRequest));
        assertEquals(ProductError.PRICE_ALREADY_EXISTS.getMessage(), exception.getMessage());
        assertEquals(ProductError.PRICE_ALREADY_EXISTS.getCode(), exception.getErrorCode());
        assertEquals(ProductError.PRICE_ALREADY_EXISTS.getHttpStatus(), exception.getHttpStatus()); // Conflict

    }

    @Test
    void addPriceShouldNotThrowException(){
        Long productId = 1L;
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setValue(new BigDecimal("20.99"));
        priceRequest.setInitDate(LocalDate.of(2020, 1, 1));
        priceRequest.setEndDate(LocalDate.of(2020, 5, 5));

        ProductResponse productResponse = mock(ProductResponse.class);
        when(productRepository.findProductById(productId)).thenReturn(Optional.of(productResponse));
        when(priceRepository.priceExistsBetweenInitDateAndEndDate(productId, priceRequest.getInitDate(), priceRequest.getEndDate())).thenReturn(false);
        PriceResponse priceResponse = productService.addPriceToProduct(productId, priceRequest);
        verify(priceRepository, times(1)).addPrice(productId, priceRequest.getValue(), priceRequest.getInitDate(), priceRequest.getEndDate());
        assertEquals(priceResponse.getValue(), priceRequest.getValue());
        assertEquals(priceResponse.getInitDate(), priceRequest.getInitDate());
        assertEquals(priceResponse.getEndDate(), priceRequest.getEndDate());

    }

    @Test
    void shouldThrowPriceNotFoundException(){
        Long productId = 1L;
        LocalDate date = LocalDate.now();

        when(priceRepository.getCurrentPrice(productId, date)).thenReturn(Optional.empty());
        ProductServiceException exception = assertThrows(ProductServiceException.class, () -> productService.getCurrentPrice(productId, date));
        assertEquals(ProductError.PRICE_NOT_FOUND.getMessage(), exception.getMessage());
        assertEquals(ProductError.PRICE_NOT_FOUND.getCode(), exception.getErrorCode());
        assertEquals(ProductError.PRICE_NOT_FOUND.getHttpStatus(), exception.getHttpStatus()); // Conflict

    }

    @Test
    void shouldGetPrice(){
        Long productId = 1L;
        LocalDate date = LocalDate.now();
        PriceValueResponse expectedPriceResponse = new PriceValueResponse();
        expectedPriceResponse.setValue(new BigDecimal("12.99"));

        when(priceRepository.getCurrentPrice(productId, date)).thenReturn(Optional.of(expectedPriceResponse));
        PriceValueResponse actualPriceResponse = productService.getCurrentPrice(productId, date);
        assertNotNull(actualPriceResponse);
        assertEquals(expectedPriceResponse.getValue(), actualPriceResponse.getValue());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound(){
        Long productId = 1L;

        when(productRepository.findProductById(productId)).thenReturn(Optional.empty());
        ProductServiceException exception = assertThrows(ProductServiceException.class, () -> productService.getProductPriceHistory(productId));
        assertEquals(ProductError.PRODUCT_NOT_FOUND.getMessage(), exception.getMessage());
        assertEquals(ProductError.PRODUCT_NOT_FOUND.getCode(), exception.getErrorCode());
        assertEquals(ProductError.PRODUCT_NOT_FOUND.getHttpStatus(), exception.getHttpStatus()); // Conflict

    }

    @Test
    void shouldGetProductPrices(){
        Long productId = 1L;
        ProductResponse expectedProductResponse = new ProductResponse();
        expectedProductResponse.setName("Rustic sarouel trousers");
        expectedProductResponse.setDescription("Mid-waist trousers with a front metal hook and zip closure. Loose-fitting.");
        PriceResponse expectedPrice = new PriceResponse();
        expectedPrice.setValue(new BigDecimal("19.99"));
        expectedPrice.setInitDate(LocalDate.now());
        List<PriceResponse> expectedPriceList = List.of(expectedPrice);
        when(productRepository.findProductById(productId)).thenReturn(Optional.of(expectedProductResponse));
        when(priceRepository.getHistoryPricesFromProduct(productId)).thenReturn(expectedPriceList);

        ProductPriceResponse actualResponse = productService.getProductPriceHistory(productId);
        assertNotNull(actualResponse);
        assertEquals(actualResponse.getPrices().size(), expectedPriceList.size());

    }

    @Test
    void shouldDeletePrice(){
        Long productId = 1L;
        Long priceId = 1L;

        productService.deletePrice(productId, priceId);
        verify(priceRepository, times(1)).deletePrice(productId, priceId);
    }

    @Test
    void shouldUpdatePrice(){
        Long productId = 1L;
        Long priceId = 1L;
        PriceRequest priceRequest = new PriceRequest();
        priceRequest.setValue(new BigDecimal("20.99"));
        priceRequest.setInitDate(LocalDate.of(2020, 1, 1));
        priceRequest.setEndDate(LocalDate.of(2020, 5, 5));

        productService.updatePrice(productId, priceId, priceRequest);
        verify(priceRepository, times(1)).updatePrice(productId, priceId, priceRequest.getValue(), priceRequest.getInitDate(), priceRequest.getEndDate());
    }

}
