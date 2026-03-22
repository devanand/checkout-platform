package com.haiilo.checkout.offer.engine;

import com.haiilo.checkout.api.money.Money;
import com.haiilo.checkout.api.offer.OfferRequest;
import com.haiilo.checkout.api.offer.OfferResult;
import com.haiilo.checkout.offer.catalog.OfferCatalog;
import com.haiilo.checkout.offer.model.ValidityPeriod;
import com.haiilo.checkout.offer.model.rule.MultiBuyOffer;
import com.haiilo.checkout.offer.model.rule.OfferType;
import com.haiilo.checkout.offer.model.rule.PercentDiscountOffer;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DefaultOfferEngineTest {

    private static final ValidityPeriod ACTIVE = new ValidityPeriod(
            LocalDate.now().minusDays(1),
            LocalDate.now().plusDays(30)
    );
    private static final ValidityPeriod EXPIRED = new ValidityPeriod(
            LocalDate.now().minusDays(30),
            LocalDate.now().minusDays(1)
    );
    private static final Money APPLE_PRICE = Money.of("0.30", "EUR");
    private static final Money BANANA_PRICE = Money.of("0.20", "EUR");

    private final OfferCatalog catalog = mock(OfferCatalog.class);
    private final DefaultOfferEngine engine = new DefaultOfferEngine(catalog);

    @Test
    void returnsNoOfferWhenNoneAssigned() {
        when(catalog.findFirstApplicable("APPLE")).thenReturn(null);

        List<OfferResult> results = engine.applyOffers(List.of(
                new OfferRequest("APPLE", 1, APPLE_PRICE)
        ));

        assertThat(results).hasSize(1);
        assertThat(results.get(0).offerApplied()).isFalse();
        assertThat(results.get(0).offerType()).isNull();
        assertThat(results.get(0).adjustedTotal().amount()).isEqualTo("0.30");
    }

    @Test
    void returnsNoOfferWhenOfferIsExpired() {
        when(catalog.findFirstApplicable("APPLE")).thenReturn(
                new MultiBuyOffer(OfferType.MULTI_BUY, "Buy 2 for 0.45", EXPIRED, 2, "0.45")
        );

        List<OfferResult> results = engine.applyOffers(List.of(
                new OfferRequest("APPLE", 2, APPLE_PRICE)
        ));

        assertThat(results.get(0).offerApplied()).isFalse();
        assertThat(results.get(0).offerType()).isNull();
    }

    @Test
    void showsOfferButDoesNotApplyWhenQuantityBelowThreshold() {
        when(catalog.findFirstApplicable("APPLE")).thenReturn(
                new MultiBuyOffer(OfferType.MULTI_BUY, "Buy 2 for 0.45", ACTIVE, 2, "0.45")
        );

        List<OfferResult> results = engine.applyOffers(List.of(
                new OfferRequest("APPLE", 1, APPLE_PRICE)
        ));

        assertThat(results.get(0).offerApplied()).isFalse();
        assertThat(results.get(0).offerType()).isEqualTo("MULTI_BUY");
        assertThat(results.get(0).adjustedTotal().amount()).isEqualTo("0.30");
    }

    @Test
    void appliesMultiBuyOfferWhenThresholdMet() {
        when(catalog.findFirstApplicable("APPLE")).thenReturn(
                new MultiBuyOffer(OfferType.MULTI_BUY, "Buy 2 for 0.45", ACTIVE, 2, "0.45")
        );

        List<OfferResult> results = engine.applyOffers(List.of(
                new OfferRequest("APPLE", 2, APPLE_PRICE)
        ));

        assertThat(results.get(0).offerApplied()).isTrue();
        assertThat(results.get(0).offerType()).isEqualTo("MULTI_BUY");
        assertThat(results.get(0).adjustedTotal().amount()).isEqualTo("0.45");
    }

    @Test
    void appliesMultiBuyOfferWithRemainder() {
        when(catalog.findFirstApplicable("APPLE")).thenReturn(
                new MultiBuyOffer(OfferType.MULTI_BUY, "Buy 2 for 0.45", ACTIVE, 2, "0.45")
        );

        List<OfferResult> results = engine.applyOffers(List.of(
                new OfferRequest("APPLE", 3, APPLE_PRICE)
        ));

        assertThat(results.get(0).offerApplied()).isTrue();
        assertThat(results.get(0).adjustedTotal().amount()).isEqualTo("0.75");
    }

    @Test
    void appliesPercentDiscountOffer() {
        when(catalog.findFirstApplicable("BANANA")).thenReturn(
                new PercentDiscountOffer(OfferType.PERCENT_DISCOUNT, "10% off", ACTIVE, 10)
        );

        List<OfferResult> results = engine.applyOffers(List.of(
                new OfferRequest("BANANA", 3, BANANA_PRICE)
        ));

        assertThat(results.get(0).offerApplied()).isTrue();
        assertThat(results.get(0).offerType()).isEqualTo("PERCENT_DISCOUNT");
        assertThat(results.get(0).adjustedTotal().amount()).isEqualTo("0.54");
    }

    @Test
    void processesMultipleRequestsIndependently() {
        when(catalog.findFirstApplicable("APPLE")).thenReturn(
                new MultiBuyOffer(OfferType.MULTI_BUY, "Buy 2 for 0.45", ACTIVE, 2, "0.45")
        );
        when(catalog.findFirstApplicable("BANANA")).thenReturn(null);

        List<OfferResult> results = engine.applyOffers(List.of(
                new OfferRequest("APPLE", 2, APPLE_PRICE),
                new OfferRequest("BANANA", 1, BANANA_PRICE)
        ));

        assertThat(results).hasSize(2);
        assertThat(results.get(0).offerApplied()).isTrue();
        assertThat(results.get(1).offerApplied()).isFalse();
    }
}
