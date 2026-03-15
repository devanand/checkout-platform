package com.haiilo.checkout.pricing.exception;

/**
 * Exception thrown when a requested product does not exist
 * in the catalog.
 */
public class UnknownProductException extends RuntimeException {

    public static final String ERROR_CODE = "UNKNOWN_PRODUCT";
    private static final String MESSAGE_TEMPLATE = "Unknown product: %s";
    public UnknownProductException(String productId) {
        super(MESSAGE_TEMPLATE.formatted(productId));
    }
}
