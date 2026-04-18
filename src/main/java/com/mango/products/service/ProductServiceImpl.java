package com.mango.products.service;

import com.mango.products.converter.ProductConverter;
import com.mango.products.exception.ProductError;
import com.mango.products.exception.ProductServiceException;
import com.mango.products.model.PriceRequest;
import com.mango.products.model.PriceResponse;
import com.mango.products.model.ProductRequest;
import com.mango.products.model.ProductResponse;
import com.mango.products.repository.PriceRepository;
import com.mango.products.repository.ProductRepository;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private void validateProductRequest(ProductRequest productRequest) {
        boolean productAlreadyExists = productRepository.productAlreadyExists(productRequest.getName(), productRequest.getDescription());
        if (productAlreadyExists) { // throw conflict exception
            throw new ProductServiceException(ProductError.PRODUCT_ALREADY_EXISTS.getMessage(), ProductError.PRODUCT_ALREADY_EXISTS.getCode(), ProductError.PRODUCT_ALREADY_EXISTS.getHttpStatus());
        }
    }

    private void validatePriceRequest(Long productId, PriceRequest priceRequest) {
        // first, we should check dates, because I think we should avoid unnecessary database queries
        if (priceRequest.getInitDate() == null) {
            throw new ProductServiceException(ProductError.INIT_DATE_IS_NULL.getMessage(), ProductError.INIT_DATE_IS_NULL.getCode(), ProductError.INIT_DATE_IS_NULL.getHttpStatus());
        }
        if (priceRequest.getEndDate() != null && priceRequest.getEndDate().isBefore(priceRequest.getInitDate())){
            throw new ProductServiceException(ProductError.INVALID_END_DATE.getMessage(), ProductError.INVALID_END_DATE.getCode(), ProductError.INVALID_END_DATE.getHttpStatus());
        }
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
}
