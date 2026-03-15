package com.haiilo.checkout.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Cart {

    private final List<CartItem> items = new ArrayList<>();

    public void add(String productId, int quantity) {
        items.add(new CartItem(productId, quantity));
    }

    public List<CartItem> items() {
        return Collections.unmodifiableList(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }
}