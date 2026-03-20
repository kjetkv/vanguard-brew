package io.code.vanguard.brew.date;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Objects;

@DisplayName("Date - Century Milestone")
@Tag("Date")
public class CenturyMilestoneTest extends BasicKataTestBase {

    @Test
    @DisplayName("When providing a standard date, adds exactly 100 years. " +
            "E.g.: '1985-10-26' returns '2085-10-26'")
    @Order(1)
    void testStandardDate() {
        verifyBasicKata(new CenturyMilestoneKata(),
                LocalDate.of(1980, 7, 31),
                LocalDate.of(2080, 7, 31),
                Objects::equals);
    }

    @Test
    @DisplayName("When the birth date is February 29th and the target year is not a leap year, safely adjusts to February 28th. " +
            "E.g.: '2004-02-29' returns '2104-02-28' (if 2104 was not a leap year)")
    @Order(2)
    void testLeapYearEdgeCase() {
        verifyBasicKata(new CenturyMilestoneKata(),
                LocalDate.of(2000, 2, 29),
                LocalDate.of(2100, 2, 28),
                Objects::equals);
    }

    @Test
    @DisplayName("When providing a date at the absolute end of the year, accurately increments the year without rolling the month. " +
            "E.g.: '1999-12-31' returns '2099-12-31'")
    @Order(3)
    void testEndOfYear() {
        verifyBasicKata(new CenturyMilestoneKata(),
                LocalDate.of(1926, 12, 31),
                LocalDate.of(2026, 12, 31),
                Objects::equals);
    }

    @Test
    @DisplayName("When providing a date from the distant past, correctly calculates the historical century mark. " +
            "E.g.: '1776-07-04' returns '1876-07-04'")
    @Order(4)
    void testHistoricalDate() {
        verifyBasicKata(new CenturyMilestoneKata(),
                LocalDate.of(1815, 12, 10),
                LocalDate.of(1915, 12, 10),
                Objects::equals);
    }

    @Test
    @DisplayName("When providing a future date, correctly calculates the century mark deep into the future. " +
            "E.g.: '2050-01-01' returns '2150-01-01'")
    @Order(5)
    void testFutureDate() {
        verifyBasicKata(new CenturyMilestoneKata(),
                LocalDate.of(2233, 3, 22),
                LocalDate.of(2333, 3, 22),
                Objects::equals);
    }
}