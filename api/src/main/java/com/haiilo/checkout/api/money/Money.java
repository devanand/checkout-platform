package com.haiilo.checkout.api.money;

import com.haiilo.checkout.api.exception.CurrencyMismatchException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;
import java.util.Objects;

public final class Money {

    private static final int SCALE = 2;
    private static final RoundingMode ROUNDING = RoundingMode.HALF_UP;

    private final BigDecimal amount;
    private final String currency;

    private Money(BigDecimal amount, String currency) {
        this.currency = validateCurrency(currency);
        this.amount = validateAmount(amount.setScale(SCALE, ROUNDING));
    }

    public static Money of(String amount, String currency) {
        Objects.requireNonNull(amount, "amount must not be null");
        try {
            return new Money(new BigDecimal(amount), currency);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid amount: " + amount);
        }
    }

    public static Money of(BigDecimal amount, String currency) {
        Objects.requireNonNull(amount, "amount must not be null");
        return new Money(amount, currency);
    }

    public static Money zero(String currency) {
        return new Money(BigDecimal.ZERO, currency);
    }

    public Money add(Money other) {
        Objects.requireNonNull(other, "other must not be null");
        assertSameCurrency(other);
        return new Money(amount.add(other.amount), currency);
    }

    public Money multiply(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("quantity must not be negative");
        }
        return new Money(amount.multiply(BigDecimal.valueOf(quantity)), currency);
    }

    public Money applyDiscount(int percentage) {
        if (percentage <= 0 || percentage >= 100) {
            throw new IllegalArgumentException("percentage must be between 1 and 99");
        }
        BigDecimal factor = BigDecimal.valueOf(100 - percentage)
                .divide(BigDecimal.valueOf(100));
        return new Money(amount.multiply(factor), currency);
    }

    public boolean isGreaterThan(Money other) {
        Objects.requireNonNull(other, "other must not be null");
        assertSameCurrency(other);
        return amount.compareTo(other.amount) > 0;
    }

    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }

    public String amount() {
        return amount.toPlainString();
    }

    public String currency() {
        return currency;
    }

    // package-private — only for JPA mappers
    BigDecimal rawAmount() {
        return amount;
    }

    private void assertSameCurrency(Money other) {
        if (!currency.equals(other.currency)) {
            throw new CurrencyMismatchException(currency, other.currency);
        }
    }

    private static BigDecimal validateAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount must not be negative");
        }
        return amount;
    }

    private static String validateCurrency(String currency) {
        Objects.requireNonNull(currency, "currency must not be null");
        try {
            Currency.getInstance(currency);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid currency: " + currency);
        }
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money money)) return false;
        return amount.compareTo(money.amount) == 0 && currency.equals(money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount.stripTrailingZeros(), currency);
    }

    @Override
    public String toString() {
        return amount() + " " + currency;
    }
}