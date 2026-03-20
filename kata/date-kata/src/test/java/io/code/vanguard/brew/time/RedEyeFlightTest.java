package io.code.vanguard.brew.time;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Objects;

import static io.code.vanguard.brew.time.RedEyeFlightKata.FlightRequest;

@DisplayName("Time - Red-Eye Flight")
@Tag("Time")
public class RedEyeFlightTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the flight departs and arrives on the exact same day, calculates standard duration. " +
            "E.g.: Depart 08:00, Arrive 10:30 returns '2H 30M'")
    @Order(1)
    void testSameDayFlight() {
        verifyBasicKata(new RedEyeFlightKata(),
                new FlightRequest(
                        LocalDateTime.of(2026, 5, 14, 14, 15),
                        LocalDateTime.of(2026, 5, 14, 17, 45)
                ),
                "3H 30M",
                Objects::equals);
    }

    @Test
    @DisplayName("When the flight crosses the midnight boundary into the next day, calculates total elapsed time accurately. " +
            "E.g.: Depart 22:00, Arrive 04:15 returns '6H 15M'")
    @Order(2)
    void testMidnightCrossing() {
        verifyBasicKata(new RedEyeFlightKata(),
                new FlightRequest(
                        LocalDateTime.of(2026, 11, 2, 23, 30),
                        LocalDateTime.of(2026, 11, 3, 6, 15)
                ),
                "6H 45M",
                Objects::equals);
    }

    @Test
    @DisplayName("When the flight spans across multiple days, accumulates the total hours seamlessly. " +
            "E.g.: Depart Day 1 at 12:00, Arrive Day 4 at 14:00 returns '74H 0M'")
    @Order(3)
    void testMultiDayTransit() {
        verifyBasicKata(new RedEyeFlightKata(),
                new FlightRequest(
                        LocalDateTime.of(1969, 7, 16, 13, 32),
                        LocalDateTime.of(1969, 7, 20, 20, 17)
                ),
                "102H 45M",
                Objects::equals);
    }

    @Test
    @DisplayName("When the flight duration lands exactly on the hour, formats the minutes as zero. " +
            "E.g.: Depart 10:00, Arrive 12:00 returns '2H 0M'")
    @Order(4)
    void testExactHourFlight() {
        verifyBasicKata(new RedEyeFlightKata(),
                new FlightRequest(
                        LocalDateTime.of(2026, 8, 1, 9, 0),
                        LocalDateTime.of(2026, 8, 1, 14, 0)
                ),
                "5H 0M",
                Objects::equals);
    }

    @Test
    @DisplayName("When the flight is incredibly short, calculates zero hours and only minutes. " +
            "E.g.: Depart 10:00, Arrive 10:12 returns '0H 12M'")
    @Order(5)
    void testVeryShortFlight() {
        verifyBasicKata(new RedEyeFlightKata(),
                new FlightRequest(
                        LocalDateTime.of(1903, 12, 17, 10, 35),
                        LocalDateTime.of(1903, 12, 17, 10, 36)
                ),
                "0H 1M",
                Objects::equals);
    }
}