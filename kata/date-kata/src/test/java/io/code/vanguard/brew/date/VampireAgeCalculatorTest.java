package io.code.vanguard.brew.date;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Objects;

@DisplayName("Date - Vampire Age Calculator")
@Tag("Date")
public class VampireAgeCalculatorTest extends BasicKataTestBase {

    @Test
    @DisplayName("When providing a birth date and a future date, calculates the exact number of full years. " +
            "E.g.: '1990-01-01' to '2020-01-01' returns 30")
    @Order(1)
    void testStandardAge() {
        verifyBasicKata(new VampireAgeCalculatorKata(),
                new VampireAgeCalculatorKata.AgeRequest(
                        LocalDate.of(1431, 11, 8),
                        LocalDate.of(2026, 11, 8)
                ),
                595,
                Objects::equals);
    }

    @Test
    @DisplayName("When the current date is before the birthday in the current year, does not count the incomplete year. " +
            "E.g.: '1900-12-31' to '2000-01-01' returns 99")
    @Order(2)
    void testBeforeBirthdayThisYear() {
        verifyBasicKata(new VampireAgeCalculatorKata(),
                new VampireAgeCalculatorKata.AgeRequest(
                        LocalDate.of(1760, 11, 7),
                        LocalDate.of(2026, 10, 31)
                ),
                265,
                Objects::equals);
    }

    @Test
    @DisplayName("When the current date is exactly the birthday, correctly includes the newly completed year. " +
            "E.g.: '1800-05-05' to '1900-05-05' returns 100")
    @Order(3)
    void testExactlyOnBirthday() {
        verifyBasicKata(new VampireAgeCalculatorKata(),
                new VampireAgeCalculatorKata.AgeRequest(
                        LocalDate.of(1901, 6, 20),
                        LocalDate.of(2005, 6, 20)
                ),
                104,
                Objects::equals);
    }

    @Test
    @DisplayName("When the subject was born on a leap year, correctly calculates the age on non-leap years without crashing. " +
            "E.g.: '1604-02-29' to '1700-03-01' returns 96")
    @Order(4)
    void testLeapYearBirthday() {
        verifyBasicKata(new VampireAgeCalculatorKata(),
                new VampireAgeCalculatorKata.AgeRequest(
                        LocalDate.of(1260, 2, 29),
                        LocalDate.of(2026, 2, 28)
                ),
                765,
                Objects::equals);
    }

    @Test
    @DisplayName("When the birth date and current date are the exact same year, returns zero. " +
            "E.g.: '2020-05-01' to '2020-10-31' returns 0")
    @Order(5)
    void testNewbornVampire() {
        verifyBasicKata(new VampireAgeCalculatorKata(),
                new VampireAgeCalculatorKata.AgeRequest(
                        LocalDate.of(2026, 1, 1),
                        LocalDate.of(2026, 8, 15)
                ),
                0,
                Objects::equals);
    }
}