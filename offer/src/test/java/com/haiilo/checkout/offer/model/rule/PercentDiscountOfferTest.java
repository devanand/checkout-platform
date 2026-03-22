package com.haiilo.checkout.offer.model.rule;

import com.haiilo.checkout.api.money.Money;
import com.haiilo.checkout.api.offer.OfferResult;
import com.haiilo.checkout.offer.model.ValidityPeriod;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PercentDiscountOfferTest {

    private static final ValidityPeriod ACTIVE = new ValidityPeriod(
            LocalDate.of(2026, 3, 1),
            LocalDate.of(2026, 3, 31)
    );
    private static final Money UNIT_PRICE = Money.of("0.20", "EUR");

    private PercentDiscountOffer offer(int percentage) {
        return new PercentDiscountOffer(OfferType.PERCENT_DISCOUNT, "10% off", ACTIVE, percentage);
    }

    @Test
    void appliesToAnyPositiveQuantity() {
        assertThat(offer(10).appliesTo(1)).isTrue();
        assertThat(offer(10).appliesTo(100)).isTrue();
    }

    @Test
    void doesNotApplyToZeroQuantity() {
        assertThat(offer(10).appliesTo(0)).isFalse();
    }

    @Test
    void appliesDiscountCorrectly() {
        OfferResult result = offer(10).apply(3, UNIT_PRICE);

        assertThat(result.adjustedTotal().amount()).isEqualTo("0.54");
        assertThat(result.offerApplied()).isTrue();
    }

    @Test
    void appliesDiscountToSingleItem() {
        OfferResult result = offer(10).apply(1, Money.of("1.00", "EUR"));

        assertThat(result.adjustedTotal().amount()).isEqualTo("0.90");
    }

    @Test
    void isActiveWithinValidityPeriod() {
        assertThat(offer(10).isActive(LocalDate.of(2026, 3, 15))).isTrue();
    }

    @Test
    void isInactiveOutsideValidityPeriod() {
        assertThat(offer(10).isActive(LocalDate.of(2026, 4, 1))).isFalse();
    }

    @Test
    void rejectsZeroPercentage() {
        assertThatThrownBy(() -> offer(0))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsHundredPercentage() {
        assertThatThrownBy(() -> offer(100))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsNegativePercentage() {
        assertThatThrownBy(() -> offer(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsNegativeQuantityOnApply() {
        assertThatThrownBy(() -> offer(10).apply(-1, UNIT_PRICE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsNullUnitPriceOnApply() {
        assertThatThrownBy(() -> offer(10).apply(1, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void returnsCorrectSummary() {
        var summary = offer(10).toAppliedOfferSummary();

        assertThat(summary.type()).isEqualTo("PERCENT_DISCOUNT");
        assertThat(summary.description()).isEqualTo("10% off");
    }
}