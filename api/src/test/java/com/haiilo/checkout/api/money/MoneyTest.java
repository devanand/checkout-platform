package com.haiilo.checkout.api.money;

import com.haiilo.checkout.api.exception.CurrencyMismatchException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MoneyTest {

    @Test
    void createsMoneyFromString() {
        Money money = Money.of("1.25", "EUR");

        assertThat(money.amount()).isEqualTo("1.25");
        assertThat(money.currency()).isEqualTo("EUR");
    }

    @Test
    void enforcesScaleOfTwo() {
        Money money = Money.of("1.5", "EUR");

        assertThat(money.amount()).isEqualTo("1.50");
    }

    @Test
    void rejectsNegativeAmount() {
        assertThatThrownBy(() -> Money.of("-0.01", "EUR"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must not be negative");
    }

    @Test
    void rejectsInvalidCurrency() {
        assertThatThrownBy(() -> Money.of("1.00", "ZZZ"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid currency");
    }

    @Test
    void rejectsInvalidAmountString() {
        assertThatThrownBy(() -> Money.of("not-a-number", "EUR"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Invalid amount");
    }

    @Test
    void addsMoneyWithSameCurrency() {
        Money result = Money.of("0.30", "EUR").add(Money.of("0.20", "EUR"));

        assertThat(result.amount()).isEqualTo("0.50");
    }

    @Test
    void throwsOnAddWithDifferentCurrency() {
        assertThatThrownBy(() -> Money.of("1.00", "EUR").add(Money.of("1.00", "USD")))
                .isInstanceOf(CurrencyMismatchException.class)
                .hasMessageContaining("EUR")
                .hasMessageContaining("USD");
    }

    @Test
    void multipliesByQuantity() {
        Money result = Money.of("0.30", "EUR").multiply(3);

        assertThat(result.amount()).isEqualTo("0.90");
    }

    @Test
    void multiplyByZeroReturnsZero() {
        Money result = Money.of("0.30", "EUR").multiply(0);

        assertThat(result.isZero()).isTrue();
    }

    @Test
    void rejectsNegativeMultiplier() {
        assertThatThrownBy(() -> Money.of("0.30", "EUR").multiply(-1))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void appliesPercentageDiscount() {
        Money result = Money.of("1.00", "EUR").applyDiscount(10);

        assertThat(result.amount()).isEqualTo("0.90");
    }

    @Test
    void appliesDiscountWithRoundingHalfUp() {
        Money result = Money.of("0.20", "EUR").multiply(3).applyDiscount(10);

        assertThat(result.amount()).isEqualTo("0.54");
    }

    @Test
    void rejectsDiscountOutsideRange() {
        assertThatThrownBy(() -> Money.of("1.00", "EUR").applyDiscount(0))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> Money.of("1.00", "EUR").applyDiscount(100))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void isZeroReturnsTrueForZeroAmount() {
        assertThat(Money.zero("EUR").isZero()).isTrue();
    }

    @Test
    void isZeroReturnsFalseForPositiveAmount() {
        assertThat(Money.of("0.01", "EUR").isZero()).isFalse();
    }

    @Test
    void isGreaterThanComparesCorrectly() {
        Money bigger = Money.of("1.00", "EUR");
        Money smaller = Money.of("0.50", "EUR");

        assertThat(bigger.isGreaterThan(smaller)).isTrue();
        assertThat(smaller.isGreaterThan(bigger)).isFalse();
    }

    @Test
    void equalsConsidersAmountAndCurrency() {
        assertThat(Money.of("1.00", "EUR")).isEqualTo(Money.of("1.00", "EUR"));
        assertThat(Money.of("1.00", "EUR")).isNotEqualTo(Money.of("1.00", "USD"));
    }

    @Test
    void toStringFormatsAmountAndCurrency() {
        assertThat(Money.of("1.50", "EUR").toString()).isEqualTo("1.50 EUR");
    }
}