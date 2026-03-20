package io.code.vanguard.brew.date;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

@DisplayName("Date - Friday the 13th Detector")
@Tag("Date")
public class FridayThe13thDetectorTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the 13th of the given month falls on a Friday, returns true. " +
            "E.g.: Year 2023, Month 10 returns true")
    @Order(1)
    void testStandardFridayThe13th() {
        verifyBasicKata(new FridayThe13thDetectorKata(),
                new FridayThe13thDetectorKata.YearMonthRequest(1980, 6),
                true,
                Objects::equals);
    }

    @Test
    @DisplayName("When the 13th of the given month falls on any other day, returns false. " +
            "E.g.: Year 2023, Month 11 returns false")
    @Order(2)
    void testNotAFriday() {
        verifyBasicKata(new FridayThe13thDetectorKata(),
                new FridayThe13thDetectorKata.YearMonthRequest(1983, 1),
                false,
                Objects::equals);
    }

    @Test
    @DisplayName("When checking a leap year, accurately determines if the shifted days create a Friday the 13th. " +
            "E.g.: Year 2020, Month 3 returns true")
    @Order(3)
    void testLeapYearFridayThe13th() {
        verifyBasicKata(new FridayThe13thDetectorKata(),
                new FridayThe13thDetectorKata.YearMonthRequest(2020, 3),
                true,
                Objects::equals);
    }

    @Test
    @DisplayName("When checking dates in the distant past, accurately calculates the historical day of the week. " +
            "E.g.: Year 1307, Month 10 returns true")
    @Order(4)
    void testHistoricalFridayThe13th() {
        verifyBasicKata(new FridayThe13thDetectorKata(),
                new FridayThe13thDetectorKata.YearMonthRequest(1307, 10),
                true,
                Objects::equals);
    }

    @Test
    @DisplayName("When checking dates in the future, accurately projects the day of the week. " +
            "E.g.: Year 2026, Month 2 returns true")
    @Order(5)
    void testFutureFridayThe13th() {
        verifyBasicKata(new FridayThe13thDetectorKata(),
                new FridayThe13thDetectorKata.YearMonthRequest(2026, 2),
                true,
                Objects::equals);
    }
}