package com.haiilo.checkout.api.exception;

public class CurrencyMismatchException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "Currency mismatch: expected %s but got %s";

    public CurrencyMismatchException(String expected, String actual) {
        super(MESSAGE_TEMPLATE.formatted(expected, actual));
    }
}