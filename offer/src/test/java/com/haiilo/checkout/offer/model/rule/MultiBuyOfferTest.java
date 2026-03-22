package com.haiilo.checkout.offer.model.rule;

import com.haiilo.checkout.api.money.Money;
import com.haiilo.checkout.api.offer.OfferResult;
import com.haiilo.checkout.offer.model.ValidityPeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MultiBuyOfferTest {

    private static final ValidityPeriod ACTIVE = new ValidityPeriod(
            LocalDate.of(2026, 3, 1),
            LocalDate.of(2026, 3, 31)
    );
    private static final Money UNIT_PRICE = Money.of("0.30", "EUR");

    private MultiBuyOffer offer(int requiredQty, String bundlePrice) {
        return new MultiBuyOffer(OfferType.MULTI_BUY, "Buy 2 for 0.45", ACTIVE, requiredQty, bundlePrice);
    }

    @Test
    void appliesToExactThreshold() {
        assertThat(offer(2, "0.45").appliesTo(2)).isTrue();
    }

    @Test
    void appliesToAboveThreshold() {
        assertThat(offer(2, "0.45").appliesTo(5)).isTrue();
    }

    @Test
    void doesNotApplyBelowThreshold() {
        assertThat(offer(2, "0.45").appliesTo(1)).isFalse();
    }

    @Test
    void calculatesBundlePriceExactly() {
        OfferResult result = offer(2, "0.45").apply(2, UNIT_PRICE);

        assertThat(result.adjustedTotal().amount()).isEqualTo("0.45");
        assertThat(result.offerApplied()).isTrue();
    }

    @Test
    void calculatesBundlePlusRemainder() {
        OfferResult result = offer(2, "0.45").apply(3, UNIT_PRICE);

        assertThat(result.adjustedTotal().amount()).isEqualTo("0.75");
    }

    @Test
    void appliesBundleMultipleTimes() {
        OfferResult result = offer(2, "0.45").apply(4, UNIT_PRICE);

        assertThat(result.adjustedTotal().amount()).isEqualTo("0.90");
    }

    @Test
    void returnsZeroForZeroQuantity() {
        OfferResult result = offer(2, "0.45").apply(0, UNIT_PRICE);

        assertThat(result.adjustedTotal().isZero()).isTrue();
    }

    @Test
    void isActiveWithinValidityPeriod() {
        assertThat(offer(2, "0.45").isActive(LocalDate.of(2026, 3, 15))).isTrue();
    }

    @Test
    void isInactiveOutsideValidityPeriod() {
        assertThat(offer(2, "0.45").isActive(LocalDate.of(2026, 4, 1))).isFalse();
    }

    @Test
    void rejectsRequiredQuantityLessThanTwo() {
        assertThatThrownBy(() -> offer(1, "0.45"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least 2");
    }

    @Test
    void rejectsNullBundlePrice() {
        assertThatThrownBy(() -> offer(2, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejectsNegativeQuantityOnApply() {
        assertThatThrownBy(() -> offer(2, "0.45").apply(-1, UNIT_PRICE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsNullUnitPriceOnApply() {
        assertThatThrownBy(() -> offer(2, "0.45").apply(2, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void returnsCorrectSummary() {
        var summary = offer(2, "0.45").toAppliedOfferSummary();

        assertThat(summary.type()).isEqualTo("MULTI_BUY");
        assertThat(summary.description()).isEqualTo("Buy 2 for 0.45");
    }
}