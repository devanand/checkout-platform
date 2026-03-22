package com.haiilo.checkout.offer.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ValidityPeriodTest {

    private static final LocalDate FROM = LocalDate.of(2026, 3, 1);
    private static final LocalDate UNTIL = LocalDate.of(2026, 3, 31);

    @Test
    void isActiveOnStartDate() {
        assertThat(new ValidityPeriod(FROM, UNTIL).isActive(FROM)).isTrue();
    }

    @Test
    void isActiveOnEndDate() {
        assertThat(new ValidityPeriod(FROM, UNTIL).isActive(UNTIL)).isTrue();
    }

    @Test
    void isActiveWithinRange() {
        assertThat(new ValidityPeriod(FROM, UNTIL).isActive(LocalDate.of(2026, 3, 15))).isTrue();
    }

    @Test
    void isInactiveBeforeStartDate() {
        assertThat(new ValidityPeriod(FROM, UNTIL).isActive(LocalDate.of(2026, 2, 28))).isFalse();
    }

    @Test
    void isInactiveAfterEndDate() {
        assertThat(new ValidityPeriod(FROM, UNTIL).isActive(LocalDate.of(2026, 4, 1))).isFalse();
    }

    @Test
    void rejectsEndDateBeforeStartDate() {
        assertThatThrownBy(() -> new ValidityPeriod(UNTIL, FROM))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void rejectsNullStartDate() {
        assertThatThrownBy(() -> new ValidityPeriod(null, UNTIL))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejectsNullEndDate() {
        assertThatThrownBy(() -> new ValidityPeriod(FROM, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void rejectsNullDateWhenCheckingActivity() {
        assertThatThrownBy(() -> new ValidityPeriod(FROM, UNTIL).isActive(null))
                .isInstanceOf(NullPointerException.class);
    }
}