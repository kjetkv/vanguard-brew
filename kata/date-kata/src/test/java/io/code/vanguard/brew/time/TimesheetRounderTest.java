package io.code.vanguard.brew.time;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.Objects;

@DisplayName("Time - Timesheet Rounder")
@Tag("Time")
public class TimesheetRounderTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the time is exactly on a 15-minute mark, returns the same time. " +
            "E.g.: '08:15' returns '08:15'")
    @Order(1)
    void testExactQuarterHour() {
        verifyBasicKata(new TimesheetRounderKata(),
                LocalTime.of(9, 30, 45),
                LocalTime.of(9, 30),
                Objects::equals);
    }

    @Test
    @DisplayName("When the time is less than 8 minutes past the mark, rounds down. " +
            "E.g.: '08:07' returns '08:00'")
    @Order(2)
    void testRoundDown() {
        verifyBasicKata(new TimesheetRounderKata(),
                LocalTime.of(13, 6),
                LocalTime.of(13, 0),
                Objects::equals);
    }

    @Test
    @DisplayName("When the time is 8 or more minutes past the mark, rounds up. " +
            "E.g.: '08:08' returns '08:15'")
    @Order(3)
    void testRoundUp() {
        verifyBasicKata(new TimesheetRounderKata(),
                LocalTime.of(14, 38),
                LocalTime.of(14, 45),
                Objects::equals);
    }

    @Test
    @DisplayName("When rounding up near the end of the hour, successfully rolls over to the next hour. " +
            "E.g.: '08:53' returns '09:00'")
    @Order(4)
    void testHourRollover() {
        verifyBasicKata(new TimesheetRounderKata(),
                LocalTime.of(16, 55),
                LocalTime.of(17, 0),
                Objects::equals);
    }

    @Test
    @DisplayName("When rounding up just before midnight, cleanly rolls over to 00:00. " +
            "E.g.: '23:56' returns '00:00'")
    @Order(5)
    void testMidnightRollover() {
        verifyBasicKata(new TimesheetRounderKata(),
                LocalTime.of(23, 53),
                LocalTime.of(0, 0),
                Objects::equals);
    }
}