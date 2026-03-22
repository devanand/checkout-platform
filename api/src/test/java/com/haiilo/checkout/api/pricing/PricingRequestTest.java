package com.haiilo.checkout.api.pricing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PricingRequestTest {

    @Test
    void createsRequestWithValidValues() {
        PricingRequest request = new PricingRequest("APPLE", 2, "EUR");

        assertThat(request.productId()).isEqualTo("APPLE");
        assertThat(request.quantity()).isEqualTo(2);
        assertThat(request.currency()).isEqualTo("EUR");
    }

    @Test
    void rejectsNullProductId() {
        assertThatThrownBy(() -> new PricingRequest(null, 1, "EUR"))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejectsNullCurrency() {
        assertThatThrownBy(() -> new PricingRequest("APPLE", 1, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejectsZeroQuantity() {
        assertThatThrownBy(() -> new PricingRequest("APPLE", 0, "EUR"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quantity must be greater than zero");
    }

    @Test
    void rejectsNegativeQuantity() {
        assertThatThrownBy(() -> new PricingRequest("APPLE", -1, "EUR"))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
