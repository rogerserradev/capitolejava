package com.mango.products.service;

import com.mango.products.converter.ProductConverter;
import com.mango.products.exception.ProductError;
import com.mango.products.exception.ProductServiceException;
import com.mango.products.model.*;
import com.mango.products.repository.PriceRepository;
import com.mango.products.repository.ProductRepository;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final PriceRepository priceRepository;
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    public ProductServiceImpl(ProductRepository productRepository, PriceRepository priceRepository) {
        this.productRepository = productRepository;
        this.priceRepository = priceRepository;
    }

    @Override
    public ProductResponse addProduct(ProductRequest productRequest) {
        validateProductRequest(productRequest);
        productRepository.addProduct(productRequest.getName(), productRequest.getDescription());
        logger.info("Product created");
        return ProductConverter.fromRequestToResponse(productRequest);
    }

    @Override
    public PriceResponse addPriceToProduct(Long productId, PriceRequest priceRequest) {
        validatePriceRequest(productId, priceRequest);
        priceRepository.addPrice(productId, priceRequest.getValue(), priceRequest.getInitDate(), priceRequest.getEndDate());
        logger.info("Price added");
        return ProductConverter.fromRequestToResponse(priceRequest);
    }

    @Override
    public PriceValueResponse getCurrentPrice(Long productId, LocalDate date) {
        return priceRepository.getCurrentPrice(productId, date)
                .orElseThrow(() -> new ProductServiceException(ProductError.PRICE_NOT_FOUND.getMessage(), ProductError.PRICE_NOT_FOUND.getCode(), ProductError.PRICE_NOT_FOUND.getHttpStatus()));
    }

    @Override
    public ProductPriceResponse getProductPriceHistory(Long productId) {
        Optional<ProductResponse> optionalProduct = productRepository.findProductById(productId);
        if (optionalProduct.isEmpty()) { // not found exception
            throw new ProductServiceException(ProductError.PRODUCT_NOT_FOUND.getMessage(), ProductError.PRODUCT_NOT_FOUND.getCode(), ProductError.PRODUCT_NOT_FOUND.getHttpStatus());
        }
        List<PriceResponse> priceHistoryList = priceRepository.getHistoryPricesFromProduct(productId);
        return ProductConverter.fromRequestToResponse(optionalProduct.get(), priceHistoryList);
    }

    @Override
    public void deletePrice(Long productId, Long priceId) {
        priceRepository.deletePrice(productId, priceId);
    }

    @Override
    public PriceResponse updatePrice(Long productId, Long priceId, PriceRequest priceRequest) {
        validateDates(priceRequest.getInitDate(), priceRequest.getEndDate());
        priceRepository.updatePrice(productId, priceId, priceRequest.getValue(), priceRequest.getInitDate(), priceRequest.getEndDate());
        return ProductConverter.fromRequestToResponse(priceRequest);
    }

    private void validateProductRequest(ProductRequest productRequest) {
        boolean productAlreadyExists = productRepository.productAlreadyExists(productRequest.getName(), productRequest.getDescription());
        if (productAlreadyExists) { // throw conflict exception
            throw new ProductServiceException(ProductError.PRODUCT_ALREADY_EXISTS.getMessage(), ProductError.PRODUCT_ALREADY_EXISTS.getCode(), ProductError.PRODUCT_ALREADY_EXISTS.getHttpStatus());
        }
    }

    private void validatePriceRequest(Long productId, PriceRequest priceRequest) {
        // first, we should check dates, because I think we should avoid unnecessary database queries
        validateDates(priceRequest.getInitDate(), priceRequest.getEndDate());
        // check if product exists in database, if not, error
        Optional<ProductResponse> optionalProduct = productRepository.findProductById(productId);
        if (optionalProduct.isEmpty()) { // not found exception
            throw new ProductServiceException(ProductError.PRODUCT_NOT_FOUND.getMessage(), ProductError.PRODUCT_NOT_FOUND.getCode(), ProductError.PRODUCT_NOT_FOUND.getHttpStatus());
        }
        // only one price per product between initDate and endDate
        boolean priceAlreadyExistsBetweenGivenDates = priceRepository.priceExistsBetweenInitDateAndEndDate(productId, priceRequest.getInitDate(), priceRequest.getEndDate());
        if (priceAlreadyExistsBetweenGivenDates) { // throw conflict exception
            throw new ProductServiceException(ProductError.PRICE_ALREADY_EXISTS.getMessage(), ProductError.PRICE_ALREADY_EXISTS.getCode(), ProductError.PRICE_ALREADY_EXISTS.getHttpStatus());
        }

    }

    private void validateDates(LocalDate initDate, LocalDate endDate) {
        if (initDate == null) {
            throw new ProductServiceException(ProductError.INIT_DATE_IS_NULL.getMessage(), ProductError.INIT_DATE_IS_NULL.getCode(), ProductError.INIT_DATE_IS_NULL.getHttpStatus());
        }
        if (endDate != null && endDate.isBefore(initDate)){
            throw new ProductServiceException(ProductError.INVALID_END_DATE.getMessage(), ProductError.INVALID_END_DATE.getCode(), ProductError.INVALID_END_DATE.getHttpStatus());
        }
    }
}
