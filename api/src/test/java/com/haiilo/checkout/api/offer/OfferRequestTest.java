package com.haiilo.checkout.api.offer;

import com.haiilo.checkout.api.money.Money;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OfferRequestTest {

    private static final Money UNIT_PRICE = Money.of("0.30", "EUR");

    @Test
    void createsOfferRequestWithValidValues() {
        OfferRequest request = new OfferRequest("APPLE", 2, UNIT_PRICE);

        assertThat(request.productId()).isEqualTo("APPLE");
        assertThat(request.quantity()).isEqualTo(2);
        assertThat(request.unitPrice()).isEqualTo(UNIT_PRICE);
    }

    @Test
    void rejectsNullProductId() {
        assertThatThrownBy(() -> new OfferRequest(null, 1, UNIT_PRICE))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejectsNullUnitPrice() {
        assertThatThrownBy(() -> new OfferRequest("APPLE", 1, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejectsZeroQuantity() {
        assertThatThrownBy(() -> new OfferRequest("APPLE", 0, UNIT_PRICE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("quantity must be greater than zero");
    }

    @Test
    void rejectsNegativeQuantity() {
        assertThatThrownBy(() -> new OfferRequest("APPLE", -1, UNIT_PRICE))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
