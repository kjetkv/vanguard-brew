package io.code.vanguard.brew.date;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Objects;

@DisplayName("Date - Sprint Planner")
@Tag("Date")
public class SprintPlannerTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the date range spans a standard work week, counts only the five weekdays. " +
            "E.g.: Monday '2026-03-02' to Friday '2026-03-06' returns 5")
    @Order(1)
    void testStandardWorkWeek() {
        verifyBasicKata(new SprintPlannerKata(),
                new SprintPlannerKata.SprintRequest(
                        LocalDate.of(1969, 7, 14),
                        LocalDate.of(1969, 7, 18)
                ),
                5,
                Objects::equals);
    }

    @Test
    @DisplayName("When the date range spans across a weekend, correctly skips Saturday and Sunday. " +
            "E.g.: Thursday '2026-03-05' to Tuesday '2026-03-10' returns 4")
    @Order(2)
    void testSpanningAWeekend() {
        verifyBasicKata(new SprintPlannerKata(),
                new SprintPlannerKata.SprintRequest(
                        LocalDate.of(2001, 12, 19),
                        LocalDate.of(2001, 12, 25)
                ),
                5,
                Objects::equals);
    }

    @Test
    @DisplayName("When the start and end dates are the exact same weekday, returns exactly one business day. " +
            "E.g.: Wednesday '2026-03-04' to Wednesday '2026-03-04' returns 1")
    @Order(3)
    void testSingleWeekday() {
        verifyBasicKata(new SprintPlannerKata(),
                new SprintPlannerKata.SprintRequest(
                        LocalDate.of(2015, 10, 21),
                        LocalDate.of(2015, 10, 21)
                ),
                1,
                Objects::equals);
    }

    @Test
    @DisplayName("When the entire date range falls exclusively on a weekend, returns zero business days. " +
            "E.g.: Saturday '2026-03-07' to Sunday '2026-03-08' returns 0")
    @Order(4)
    void testOnlyWeekendDays() {
        verifyBasicKata(new SprintPlannerKata(),
                new SprintPlannerKata.SprintRequest(
                        LocalDate.of(1985, 10, 26),
                        LocalDate.of(1985, 10, 27)
                ),
                0,
                Objects::equals);
    }

    @Test
    @DisplayName("When the date range covers multiple consecutive weeks, skips all weekends accurately. " +
            "E.g.: Monday '2026-03-02' to Sunday '2026-03-15' returns 10")
    @Order(5)
    void testMultipleWeeks() {
        verifyBasicKata(new SprintPlannerKata(),
                new SprintPlannerKata.SprintRequest(
                        LocalDate.of(1977, 5, 25),
                        LocalDate.of(1977, 6, 8)
                ),
                11,
                Objects::equals);
    }
}