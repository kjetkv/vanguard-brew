package io.code.vanguard.brew.exception;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Validators.verifySameExceptionClassAndMessage;
import static io.code.vanguard.brew.exception.JammedHopperKata.InvalidHopperMassException;
import static io.code.vanguard.brew.exception.JammedHopperKata.LoadedHopper;

@DisplayName("Exceptions - Jammed Hopper")
@Tag("Resiliency")
@Tag("Exceptions")
public class JammedHopperTest extends BasicKataTestBase {

    @Test
    @DisplayName("Successfully loads an empty 0g batch into the hopper.")
    @Order(1)
    void testValidLoadZeroGrams() {
        verifyBasicKata(
                new JammedHopperKata(),
                0,
                new LoadedHopper(0),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully loads a minimal 1g batch into the hopper.")
    @Order(2)
    void testValidLoadOneGram() {
        verifyBasicKata(
                new JammedHopperKata(),
                1,
                new LoadedHopper(1),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully loads a standard 500g batch into the hopper.")
    @Order(3)
    void testValidLoadMediumBatch() {
        verifyBasicKata(
                new JammedHopperKata(),
                500,
                new LoadedHopper(500),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully loads a 999g batch just below capacity.")
    @Order(4)
    void testValidLoadNearMaxCapacity() {
        verifyBasicKata(
                new JammedHopperKata(),
                999,
                new LoadedHopper(999),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully loads exactly 1000g reaching maximum capacity.")
    @Order(5)
    void testValidLoadExactMaxCapacity() {
        verifyBasicKata(
                new JammedHopperKata(),
                1000,
                new LoadedHopper(1000),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Fails when -1g is requested.")
    @Order(6)
    void testNegativeMassThrowsExceptionNegativeOne() {
        verifyException(
                () -> new JammedHopperKata().solve(-1),
                new InvalidHopperMassException("Cannot load negative mass: -1g."),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Fails when -50g is requested.")
    @Order(7)
    void testNegativeMassThrowsExceptionNegativeFifty() {
        verifyException(
                () -> new JammedHopperKata().solve(-50),
                new InvalidHopperMassException("Cannot load negative mass: -50g."),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Fails when -1000g is requested.")
    @Order(8)
    void testNegativeMassThrowsExceptionNegativeThousand() {
        verifyException(
                () -> new JammedHopperKata().solve(-1000),
                new InvalidHopperMassException("Cannot load negative mass: -1000g."),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Fails when capacity exceeds by 1g.")
    @Order(9)
    void testOverCapacityThrowsExceptionOneOver() {
        verifyException(
                () -> new JammedHopperKata().solve(1001),
                new InvalidHopperMassException("Hopper capacity exceeded. Requested 1001g, maximum is 1000g."),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Fails when capacity is highly exceeded.")
    @Order(10)
    void testOverCapacityThrowsExceptionMassive() {
        verifyException(
                () -> new JammedHopperKata().solve(5000),
                new InvalidHopperMassException("Hopper capacity exceeded. Requested 5000g, maximum is 1000g."),
                verifySameExceptionClassAndMessage
        );
    }
}
