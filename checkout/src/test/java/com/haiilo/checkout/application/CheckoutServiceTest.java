package com.haiilo.checkout.application;

import com.haiilo.checkout.api.money.Money;
import com.haiilo.checkout.api.pricing.PricingResponse;
import com.haiilo.checkout.domain.Cart;
import com.haiilo.checkout.pricing.PricingService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CheckoutServiceTest {

    private final PricingService pricingService = mock(PricingService.class);
    private final CheckoutService checkoutService = new CheckoutService(pricingService);

    @Test
    void returnsEmptyResultForEmptyCart() {
        when(pricingService.price(anyList())).thenReturn(List.of());

        CheckoutResult result = checkoutService.checkout(new Cart());

        assertThat(result.items()).isEmpty();
        assertThat(result.total().isZero()).isTrue();
        assertThat(result.total().currency()).isEqualTo("EUR");
    }

    @Test
    void calculatesTotalForSingleItemWithNoOffer() {
        Cart cart = new Cart();
        cart.add("APPLE", 1);

        when(pricingService.price(anyList())).thenReturn(List.of(
                new PricingResponse(
                        "APPLE", 1,
                        Money.of("0.30", "EUR"),
                        Money.of("0.30", "EUR"),
                        null, null, false)
        ));

        CheckoutResult result = checkoutService.checkout(cart);

        assertThat(result.items()).hasSize(1);
        assertThat(result.total().amount()).isEqualTo("0.30");
    }

    @Test
    void sumsTotalsAcrossMultipleItems() {
        Cart cart = new Cart();
        cart.add("APPLE", 2);
        cart.add("BANANA", 3);

        when(pricingService.price(anyList())).thenReturn(List.of(
                new PricingResponse(
                        "APPLE", 2,
                        Money.of("0.30", "EUR"),
                        Money.of("0.45", "EUR"),
                        "MULTI_BUY", "Buy 2 for 0.45", true),
                new PricingResponse(
                        "BANANA", 3,
                        Money.of("0.20", "EUR"),
                        Money.of("0.54", "EUR"),
                        "PERCENT_DISCOUNT", "10% off", true)
        ));

        CheckoutResult result = checkoutService.checkout(cart);

        assertThat(result.total().amount()).isEqualTo("0.99");
        assertThat(result.items()).hasSize(2);
    }

    @Test
    void mapsOfferFieldsCorrectly() {
        Cart cart = new Cart();
        cart.add("APPLE", 2);

        when(pricingService.price(anyList())).thenReturn(List.of(
                new PricingResponse(
                        "APPLE", 2,
                        Money.of("0.30", "EUR"),
                        Money.of("0.45", "EUR"),
                        "MULTI_BUY", "Buy 2 for 0.45", true)
        ));

        CheckoutResult result = checkoutService.checkout(cart);
        CheckoutLine line = result.items().get(0);

        assertThat(line.offerType()).isEqualTo("MULTI_BUY");
        assertThat(line.offerDescription()).isEqualTo("Buy 2 for 0.45");
        assertThat(line.offerApplied()).isTrue();
    }

    @Test
    void rejectsNullCart() {
        assertThatThrownBy(() -> checkoutService.checkout(null))
                .isInstanceOf(NullPointerException.class);
    }
}
