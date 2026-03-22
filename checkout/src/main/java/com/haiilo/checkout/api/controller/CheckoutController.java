package com.haiilo.checkout.api.controller;

import com.haiilo.checkout.api.dto.CheckoutRequest;
import com.haiilo.checkout.api.dto.CheckoutResponse;
import com.haiilo.checkout.application.CheckoutResult;
import com.haiilo.checkout.application.CheckoutService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/checkout")
public class CheckoutController {

    private final CheckoutService checkoutService;

    public CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = Objects.requireNonNull(checkoutService, "checkoutService must not be null");
    }

    @PostMapping
    public CheckoutResponse checkout(@Valid @RequestBody CheckoutRequest request) {
        CheckoutResult result = checkoutService.checkout(request.toCart());
        return CheckoutResponse.fromResult(result);
    }
}
