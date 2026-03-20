package io.code.vanguard.brew.date;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Objects;

@DisplayName("Date - Time Traveler's Parser")
@Tag("Date")
public class TimeTravelersParserTest extends BasicKataTestBase {

    @Test
    @DisplayName("When providing a standard date string, parses it into a LocalDate. " +
            "E.g.: '15-Aug-1995' returns 1995-08-15")
    @Order(1)
    void testStandardDate() {
        verifyBasicKata(new TimeTravelersParserKata(),
                "26-Oct-1985",
                LocalDate.of(1985, 10, 26),
                Objects::equals);
    }

    @Test
    @DisplayName("When the day is a single digit, successfully parses the zero-padded string. " +
            "E.g.: '05-Nov-1955' returns 1955-11-05")
    @Order(2)
    void testZeroPaddedDay() {
        verifyBasicKata(new TimeTravelersParserKata(),
                "04-Jul-1776",
                LocalDate.of(1776, 7, 4),
                Objects::equals);
    }

    @Test
    @DisplayName("When providing a leap year date, accurately parses and validates the 29th of February. " +
            "E.g.: '29-Feb-2024' returns 2024-02-29")
    @Order(3)
    void testLeapYearDate() {
        verifyBasicKata(new TimeTravelersParserKata(),
                "29-Feb-2020",
                LocalDate.of(2020, 2, 29),
                Objects::equals);
    }

    @Test
    @DisplayName("When providing a date at the extreme end of the year, parses the month and day correctly. " +
            "E.g.: '31-Dec-1999' returns 1999-12-31")
    @Order(4)
    void testEndOfYearDate() {
        verifyBasicKata(new TimeTravelersParserKata(),
                "31-Dec-1999",
                LocalDate.of(1999, 12, 31),
                Objects::equals);
    }

    @Test
    @DisplayName("When providing a date far into the future, successfully parses the year. " +
            "E.g.: '01-Jan-3000' returns 3000-01-01")
    @Order(5)
    void testDeepFutureDate() {
        verifyBasicKata(new TimeTravelersParserKata(),
                "02-Feb-2222",
                LocalDate.of(2222, 2, 2),
                Objects::equals);
    }
}