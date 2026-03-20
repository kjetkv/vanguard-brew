package io.code.vanguard.brew.date;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Objects;

@DisplayName("Date - Subscription Validator")
@Tag("Date")
public class SubscriptionValidatorTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the check date is well before the expiration date, returns true. " +
            "E.g.: Start '2020-01-01', duration 6 months, check '2020-03-01' returns true")
    @Order(1)
    void testActiveSubscription() {
        verifyBasicKata(new SubscriptionValidatorKata(),
                new SubscriptionValidatorKata.SubscriptionRequest(
                        LocalDate.of(2004, 11, 23),
                        6,
                        LocalDate.of(2005, 1, 15)
                ),
                true,
                Objects::equals);
    }

    @Test
    @DisplayName("When the check date is strictly after the expiration date, returns false. " +
            "E.g.: Start '2020-01-01', duration 1 month, check '2020-05-01' returns false")
    @Order(2)
    void testExpiredSubscription() {
        verifyBasicKata(new SubscriptionValidatorKata(),
                new SubscriptionValidatorKata.SubscriptionRequest(
                        LocalDate.of(1997, 8, 29),
                        12,
                        LocalDate.of(1999, 1, 1)
                ),
                false,
                Objects::equals);
    }

    @Test
    @DisplayName("When the check date lands exactly on the expiration date, returns false as it is not strictly before. " +
            "E.g.: Start '2020-01-01', duration 1 month, check '2020-02-01' returns false")
    @Order(3)
    void testExactExpirationBoundary() {
        verifyBasicKata(new SubscriptionValidatorKata(),
                new SubscriptionValidatorKata.SubscriptionRequest(
                        LocalDate.of(2077, 12, 10),
                        24,
                        LocalDate.of(2079, 12, 10)
                ),
                false,
                Objects::equals);
    }

    @Test
    @DisplayName("When a subscription starts at the end of a long month, successfully caps the expiration at the end of a shorter month. " +
            "E.g.: Start '2024-01-31', duration 1 month, check '2024-03-01' returns false")
    @Order(4)
    void testMonthEndEdgeCase() {
        verifyBasicKata(new SubscriptionValidatorKata(),
                new SubscriptionValidatorKata.SubscriptionRequest(
                        LocalDate.of(2024, 1, 31),
                        1,
                        LocalDate.of(2024, 3, 1)
                ),
                false,
                Objects::equals);
    }

    @Test
    @DisplayName("When the check date is the exact same day the subscription starts, returns true. " +
            "E.g.: Start '2020-01-01', duration 1 month, check '2020-01-01' returns true")
    @Order(5)
    void testCheckOnStartDate() {
        verifyBasicKata(new SubscriptionValidatorKata(),
                new SubscriptionValidatorKata.SubscriptionRequest(
                        LocalDate.of(2026, 2, 26),
                        1,
                        LocalDate.of(2026, 2, 26)
                ),
                true,
                Objects::equals);
    }
}