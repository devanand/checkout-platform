package com.haiilo.checkout.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartTest {

    @Test
    void startsEmpty() {
        Cart cart = new Cart();

        assertThat(cart.isEmpty()).isTrue();
        assertThat(cart.items()).isEmpty();
    }

    @Test
    void addsItemToCart() {
        Cart cart = new Cart();
        cart.add("APPLE", 2);

        assertThat(cart.isEmpty()).isFalse();
        assertThat(cart.items()).hasSize(1);
        assertThat(cart.items().get(0).productId()).isEqualTo("APPLE");
        assertThat(cart.items().get(0).quantity()).isEqualTo(2);
    }

    @Test
    void addsMultipleDifferentItems() {
        Cart cart = new Cart();
        cart.add("APPLE", 2);
        cart.add("BANANA", 3);

        assertThat(cart.items()).hasSize(2);
    }

    @Test
    void addsSameProductAsSeparateLines() {
        Cart cart = new Cart();
        cart.add("APPLE", 2);
        cart.add("APPLE", 3);

        assertThat(cart.items()).hasSize(2);
    }

    @Test
    void itemsListIsUnmodifiable() {
        Cart cart = new Cart();
        cart.add("APPLE", 1);

        assertThatThrownBy(() -> cart.items().clear())
                .isInstanceOf(UnsupportedOperationException.class);
    }
}
