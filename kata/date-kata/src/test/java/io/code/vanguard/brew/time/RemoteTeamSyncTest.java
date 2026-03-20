package io.code.vanguard.brew.time;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Objects;

import static io.code.vanguard.brew.time.RemoteTeamSyncKata.SyncRequest;

@DisplayName("Time - Remote Team Sync")
@Tag("Time")
public class RemoteTeamSyncTest extends BasicKataTestBase {

    @Test
    @DisplayName("When converting between standard whole-hour timezones, accurately shifts the local time. " +
            "E.g.: 14:00 in UTC is 15:00 in Europe/Oslo")
    @Order(1)
    void testStandardWholeHourConversion() {
        verifyBasicKata(new RemoteTeamSyncKata(),
                new SyncRequest(
                        LocalDateTime.of(2026, 1, 15, 14, 0),
                        "UTC",
                        "Europe/Oslo"
                ),
                LocalDateTime.of(2026, 1, 15, 15, 0),
                Objects::equals);
    }

    @Test
    @DisplayName("When converting to a timezone with a fractional offset, calculates the exact minutes correctly. " +
            "E.g.: 12:00 in UTC is 17:30 in Asia/Kolkata")
    @Order(2)
    void testFractionalTimezoneOffsets() {
        verifyBasicKata(new RemoteTeamSyncKata(),
                new SyncRequest(
                        LocalDateTime.of(2026, 4, 10, 12, 0),
                        "UTC",
                        "Asia/Kolkata"
                ),
                LocalDateTime.of(2026, 4, 10, 17, 30),
                Objects::equals);
    }

    @Test
    @DisplayName("When a conversion crosses the International Date Line backwards, the target local date is the previous day. " +
            "E.g.: 09:00 on Tuesday in Tokyo is 14:00 on Monday in Honolulu")
    @Order(3)
    void testInternationalDateLine() {
        verifyBasicKata(new RemoteTeamSyncKata(),
                new SyncRequest(
                        LocalDateTime.of(2026, 12, 8, 9, 0),
                        "Asia/Tokyo",
                        "Pacific/Honolulu"
                ),
                LocalDateTime.of(2026, 12, 7, 14, 0),
                Objects::equals);
    }

    @Test
    @DisplayName("When a conversion crosses the midnight boundary forward, the target local date rolls to the next day. " +
            "E.g.: 22:00 in New York is 04:00 the next day in Paris")
    @Order(4)
    void testMidnightBoundaryForward() {
        verifyBasicKata(new RemoteTeamSyncKata(),
                new SyncRequest(
                        LocalDateTime.of(2026, 7, 4, 22, 0),
                        "America/New_York",
                        "Europe/Paris"
                ),
                LocalDateTime.of(2026, 7, 5, 4, 0),
                Objects::equals);
    }

    @Test
    @DisplayName("When the source time falls into a Spring-Forward DST gap, intelligently adjusts the time forward before converting. " +
            "E.g.: 02:30 AM in NY on DST transition day does not exist; it shifts to 03:30 AM EDT, then converts to UTC")
    @Order(5)
    void testDstGapAdjustment() {
        verifyBasicKata(new RemoteTeamSyncKata(),
                new SyncRequest(
                        LocalDateTime.of(2026, 3, 8, 2, 30),
                        "America/New_York",
                        "UTC"
                ),
                LocalDateTime.of(2026, 3, 8, 7, 30),
                Objects::equals);
    }
}