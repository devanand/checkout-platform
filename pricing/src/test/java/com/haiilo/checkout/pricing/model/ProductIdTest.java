package com.haiilo.checkout.pricing.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ProductIdTest {

    @Test
    void normalisesToUppercaseAndTrims() {
        ProductId productId = ProductId.of(" apple ");

        assertThat(productId.value()).isEqualTo("APPLE");
    }

    @Test
    void alreadyUppercaseValueUnchanged() {
        assertThat(ProductId.of("APPLE").value()).isEqualTo("APPLE");
    }

    @Test
    void equalityIsValueBased() {
        assertThat(ProductId.of("apple")).isEqualTo(ProductId.of("APPLE"));
    }

    @Test
    void rejectsNull() {
        assertThatThrownBy(() -> ProductId.of(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejectsBlank() {
        assertThatThrownBy(() -> ProductId.of("   "))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsEmpty() {
        assertThatThrownBy(() -> ProductId.of(""))
                .isInstanceOf(IllegalArgumentException.class);
    }
}