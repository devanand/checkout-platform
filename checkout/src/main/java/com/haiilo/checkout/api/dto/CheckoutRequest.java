package com.haiilo.checkout.api.dto;

import com.haiilo.checkout.domain.Cart;
import com.haiilo.checkout.domain.CartItem;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;
import java.util.Objects;

public record CheckoutRequest(
        @NotEmpty List<@Valid CheckoutItemRequest> items
) {
    public Cart toCart() {
        Objects.requireNonNull(items, "items must not be null");

        Cart cart = new Cart();
        for (CheckoutItemRequest item : items) {
            cart.add(item.productId(), item.quantity());
        }
        return cart;
    }
}