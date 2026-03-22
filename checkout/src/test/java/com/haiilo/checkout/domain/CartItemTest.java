package com.haiilo.checkout.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CartItemTest {

    @Test
    void createsCartItemWithValidValues() {
        CartItem item = new CartItem("APPLE", 2);

        assertThat(item.productId()).isEqualTo("APPLE");
        assertThat(item.quantity()).isEqualTo(2);
    }

    @Test
    void rejectsNullProductId() {
        assertThatThrownBy(() -> new CartItem(null, 1))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejectsBlankProductId() {
        assertThatThrownBy(() -> new CartItem("  ", 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must not be blank");
    }

    @Test
    void rejectsZeroQuantity() {
        assertThatThrownBy(() -> new CartItem("APPLE", 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must be greater than zero");
    }

    @Test
    void rejectsNegativeQuantity() {
        assertThatThrownBy(() -> new CartItem("APPLE", -1))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
