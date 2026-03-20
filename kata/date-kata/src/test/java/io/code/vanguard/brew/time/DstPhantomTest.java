package io.code.vanguard.brew.time;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Objects;

import static io.code.vanguard.brew.time.DstPhantomKata.PhantomRequest;

@DisplayName("Time - DST Phantom")
@Tag("Time")
public class DstPhantomTest extends BasicKataTestBase {

    @Test
    @DisplayName("When adding exactly 24 hours during a standard week, the local time remains identical the next day. " +
            "E.g.: 10:00 + 24h = 10:00 next day")
    @Order(1)
    void testStandard24HourAddition() {
        verifyBasicKata(new DstPhantomKata(),
                new PhantomRequest(
                        ZonedDateTime.of(2026, 6, 15, 12, 0, 0, 0, ZoneId.of("America/New_York")),
                        24
                ),
                ZonedDateTime.of(2026, 6, 16, 12, 0, 0, 0, ZoneId.of("America/New_York")),
                Objects::equals);
    }

    @Test
    @DisplayName("When adding exactly 24 hours across a Fall Back boundary, the clock lands one hour earlier. " +
            "E.g.: 10:00 + 24h = 09:00 next day because 25 hours exist in that calendar day")
    @Order(2)
    void testFallBackBoundary() {
        verifyBasicKata(new DstPhantomKata(),
                new PhantomRequest(
                        ZonedDateTime.of(2026, 10, 31, 14, 0, 0, 0, ZoneId.of("America/New_York")),
                        24
                ),
                ZonedDateTime.of(2026, 11, 1, 13, 0, 0, 0, ZoneId.of("America/New_York")),
                Objects::equals);
    }

    @Test
    @DisplayName("When adding exactly 24 hours across a Spring Forward boundary, the clock lands one hour later. " +
            "E.g.: 10:00 + 24h = 11:00 next day because only 23 hours exist in that calendar day")
    @Order(3)
    void testSpringForwardBoundary() {
        verifyBasicKata(new DstPhantomKata(),
                new PhantomRequest(
                        ZonedDateTime.of(2026, 3, 7, 8, 30, 0, 0, ZoneId.of("America/New_York")),
                        24
                ),
                ZonedDateTime.of(2026, 3, 8, 9, 30, 0, 0, ZoneId.of("America/New_York")),
                Objects::equals);
    }

    @Test
    @DisplayName("When crossing a European Fall Back boundary, accurately calculates the shift using European rules. " +
            "E.g.: 12:00 + 24h = 11:00 next day on the last Sunday of October")
    @Order(4)
    void testEuropeanDstBoundary() {
        verifyBasicKata(new DstPhantomKata(),
                new PhantomRequest(
                        ZonedDateTime.of(2026, 10, 24, 15, 0, 0, 0, ZoneId.of("Europe/Oslo")),
                        24
                ),
                ZonedDateTime.of(2026, 10, 25, 14, 0, 0, 0, ZoneId.of("Europe/Oslo")),
                Objects::equals);
    }

    @Test
    @DisplayName("When adding multiple days of chronological hours across a boundary, accurately shifts the final wall-clock time. " +
            "E.g.: 10:00 + 48h crossing a fall back boundary returns 09:00 two days later")
    @Order(5)
    void testMultipleDaysAcrossBoundary() {
        verifyBasicKata(new DstPhantomKata(),
                new PhantomRequest(
                        ZonedDateTime.of(2026, 10, 30, 20, 0, 0, 0, ZoneId.of("America/Chicago")),
                        48
                ),
                ZonedDateTime.of(2026, 11, 1, 19, 0, 0, 0, ZoneId.of("America/Chicago")),
                Objects::equals);
    }
}
