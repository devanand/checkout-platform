package com.haiilo.checkout.api.pricing;

import com.haiilo.checkout.api.money.Money;
import com.haiilo.checkout.api.offer.OfferResult;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PricingResponseTest {

    private static final Money UNIT_PRICE = Money.of("0.30", "EUR");
    private static final Money LINE_TOTAL = Money.of("0.45", "EUR");

    @Test
    void createsResponseWithValidValues() {
        PricingResponse response = new PricingResponse(
                "APPLE", 2, UNIT_PRICE, LINE_TOTAL, "MULTI_BUY", "Buy 2 for 0.45", true
        );

        assertThat(response.productId()).isEqualTo("APPLE");
        assertThat(response.quantity()).isEqualTo(2);
        assertThat(response.unitPrice()).isEqualTo(UNIT_PRICE);
        assertThat(response.lineTotal()).isEqualTo(LINE_TOTAL);
        assertThat(response.offerType()).isEqualTo("MULTI_BUY");
        assertThat(response.offerApplied()).isTrue();
    }

    @Test
    void rejectsNullProductId() {
        assertThatThrownBy(() -> new PricingResponse(null, 1, UNIT_PRICE, LINE_TOTAL, null, null, false))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejectsNullUnitPrice() {
        assertThatThrownBy(() -> new PricingResponse("APPLE", 1, null, LINE_TOTAL, null, null, false))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejectsNullLineTotal() {
        assertThatThrownBy(() -> new PricingResponse("APPLE", 1, UNIT_PRICE, null, null, null, false))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void ofFactoryMapsFromRequestAndOfferResult() {
        PricingRequest request = new PricingRequest("APPLE", 2, "EUR");
        OfferResult offerResult = new OfferResult(LINE_TOTAL, "MULTI_BUY", "Buy 2 for 0.45", true);

        PricingResponse response = PricingResponse.of(request, UNIT_PRICE, offerResult);

        assertThat(response.productId()).isEqualTo("APPLE");
        assertThat(response.quantity()).isEqualTo(2);
        assertThat(response.unitPrice()).isEqualTo(UNIT_PRICE);
        assertThat(response.lineTotal()).isEqualTo(LINE_TOTAL);
        assertThat(response.offerType()).isEqualTo("MULTI_BUY");
        assertThat(response.offerApplied()).isTrue();
    }

    @Test
    void ofFactoryHandlesNoOffer() {
        PricingRequest request = new PricingRequest("APPLE", 1, "EUR");
        OfferResult offerResult = OfferResult.noOffer(Money.of("0.30", "EUR"));

        PricingResponse response = PricingResponse.of(request, UNIT_PRICE, offerResult);

        assertThat(response.offerType()).isNull();
        assertThat(response.offerApplied()).isFalse();
    }
}
