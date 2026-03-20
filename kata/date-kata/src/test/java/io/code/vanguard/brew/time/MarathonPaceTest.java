package io.code.vanguard.brew.time;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Objects;

import static io.code.vanguard.brew.time.MarathonPaceKata.PaceRequest;

@DisplayName("Time - Marathon Pace")
@Tag("Time")
public class MarathonPaceTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the total time divides evenly into minutes, returns a clean minute-based pace. " +
            "E.g.: 50 minutes for 10km returns 5 minutes per km")
    @Order(1)
    void testEvenMinutePace() {
        verifyBasicKata(new MarathonPaceKata(),
                new PaceRequest(Duration.ofMinutes(50), 10),
                Duration.ofMinutes(5),
                Objects::equals);
    }

    @Test
    @DisplayName("When calculating a full marathon, seamlessly divides hours and minutes into a precise pace. " +
            "E.g.: 2 hours 56 minutes 24 seconds for 42km returns 4 minutes 12 seconds per km")
    @Order(2)
    void testMarathonDistance() {
        verifyBasicKata(new MarathonPaceKata(),
                new PaceRequest(
                        Duration.ofHours(2).plusMinutes(56).plusSeconds(24),
                        42
                ),
                Duration.ofMinutes(4).plusSeconds(12),
                Objects::equals);
    }

    @Test
    @DisplayName("When the division requires splitting leftover seconds, accurately calculates the exact seconds per km. " +
            "E.g.: 24 minutes 10 seconds for 5km returns 4 minutes 50 seconds per km")
    @Order(3)
    void testFractionalSecondsPace() {
        verifyBasicKata(new MarathonPaceKata(),
                new PaceRequest(
                        Duration.ofMinutes(24).plusSeconds(10),
                        5
                ),
                Duration.ofMinutes(4).plusSeconds(50),
                Objects::equals);
    }

    @Test
    @DisplayName("When the distance is exactly 1km, returns the exact original duration. " +
            "E.g.: 3 minutes 45 seconds for 1km returns 3 minutes 45 seconds per km")
    @Order(4)
    void testSingleKilometer() {
        verifyBasicKata(new MarathonPaceKata(),
                new PaceRequest(
                        Duration.ofMinutes(3).plusSeconds(45),
                        1
                ),
                Duration.ofMinutes(3).plusSeconds(45),
                Objects::equals);
    }

    @Test
    @DisplayName("When calculating an ultra-marathon, seamlessly divides large hour counts down into a minute pace. " +
            "E.g.: 8 hours 20 minutes for 100km returns 5 minutes per km")
    @Order(5)
    void testUltraMarathon() {
        verifyBasicKata(new MarathonPaceKata(),
                new PaceRequest(
                        Duration.ofHours(8).plusMinutes(20),
                        100
                ),
                Duration.ofMinutes(5),
                Objects::equals);
    }
}