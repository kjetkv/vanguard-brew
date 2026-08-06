package io.code.vanguard.brew.retries;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Validators.verifyWrappedSameExceptionClassAndMessage;
import static io.code.vanguard.brew.retries.MotorKickstartKata.ConveyorHaltedRuntimeException;
import static io.code.vanguard.brew.retries.MotorKickstartKata.HeavyMotor;
import static io.code.vanguard.brew.retries.MotorKickstartKata.MotorStallException;

@DisplayName("Retry Patterns - Motor Kickstart")
@Tag("Resiliency")
@Tag("Retries")
public class MotorKickstartTest extends BasicKataTestBase {

    private static class MockMotor implements HeavyMotor {
        private int attempts = 0;
        private final int requiredAttemptsForSuccess;
        private final String successMessage;
        private final MotorStallException stallFault;

        public MockMotor(int requiredAttemptsForSuccess, String successMessage, MotorStallException stallFault) {
            this.requiredAttemptsForSuccess = requiredAttemptsForSuccess;
            this.successMessage = successMessage;
            this.stallFault = stallFault;
        }

        @Override
        public String ignite() throws MotorStallException {
            attempts++;
            if (attempts < requiredAttemptsForSuccess) {
                throw stallFault;
            }
            return successMessage;
        }

        public int getAttempts() {
            return attempts;
        }

        @Override
        public String toString() {
            return "HeavyMotor[Ignition Attempts: " + attempts + "]";
        }
    }

    @Test
    @DisplayName("Successfully ignites a perfectly maintained motor on the very first attempt.")
    @Order(1)
    void testSucceedsFirstTime() {
        var motor = new MockMotor(1, "ENGINE_PURRING", new MotorStallException("Cold start failure."));

        verifyBasicKata(
                new MotorKickstartKata(),
                motor,
                "ENGINE_PURRING",
                Objects::equals
        );

        verifyClass(
                motor,
                MockMotor::getAttempts,
                1,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Overcomes a minor mechanical stutter and successfully ignites on the second attempt.")
    @Order(2)
    void testSucceedsSecondTime() {
        var motor = new MockMotor(2, "ENGINE_PURRING", new MotorStallException("Cold start failure."));

        verifyBasicKata(
                new MotorKickstartKata(),
                motor,
                "ENGINE_PURRING",
                Objects::equals
        );

        verifyClass(
                motor,
                MockMotor::getAttempts,
                2,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Pushes through severe gear resistance and barely ignites on the third and final attempt.")
    @Order(3)
    void testSucceedsThirdTime() {
        var motor = new MockMotor(3, "ENGINE_PURRING", new MotorStallException("Cold start failure."));

        verifyBasicKata(
                new MotorKickstartKata(),
                motor,
                "ENGINE_PURRING",
                Objects::equals
        );

        verifyClass(
                motor,
                MockMotor::getAttempts,
                3,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Safely halts the conveyor line when a rusted motor completely fails after three consecutive attempts.")
    @Order(4)
    void testFailsAfterThreeAttempts() {
        var motor = new MockMotor(4, "ENGINE_PURRING", new MotorStallException("Gears rusted shut."));

        verifyException(
                () -> new MotorKickstartKata().solve(motor),
                new ConveyorHaltedRuntimeException(
                        "Conveyor motor completely failed after 3 attempts.",
                        new MotorStallException("Gears rusted shut.")
                ),
                verifyWrappedSameExceptionClassAndMessage
        );

        verifyClass(
                motor,
                MockMotor::getAttempts,
                3,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully kicks over a light 500g load line instantly on the first try.")
    @Order(5)
    void testSucceedsFirstTimeLightLoad() {
        var motor = new MockMotor(1, "LOAD_500G_MOVING", new MotorStallException("Torque failure."));

        verifyBasicKata(
                new MotorKickstartKata(),
                motor,
                "LOAD_500G_MOVING",
                Objects::equals
        );

        verifyClass(
                motor,
                MockMotor::getAttempts,
                1,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully kicks over a heavy 1000g load line after initial torque resistance on the second try.")
    @Order(6)
    void testSucceedsSecondTimeHeavyLoad() {
        var motor = new MockMotor(2, "LOAD_1000G_MOVING", new MotorStallException("Torque failure."));

        verifyBasicKata(
                new MotorKickstartKata(),
                motor,
                "LOAD_1000G_MOVING",
                Objects::equals
        );

        verifyClass(
                motor,
                MockMotor::getAttempts,
                2,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully kicks over a massive 5000g industrial payload on the absolute final try.")
    @Order(7)
    void testSucceedsThirdTimeIndustrialLoad() {
        var motor = new MockMotor(3, "LOAD_5000G_MOVING", new MotorStallException("Torque failure."));

        verifyBasicKata(
                new MotorKickstartKata(),
                motor,
                "LOAD_5000G_MOVING",
                Objects::equals
        );

        verifyClass(
                motor,
                MockMotor::getAttempts,
                3,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Safely halts the conveyor line when a severed drive belt causes three consecutive stalls.")
    @Order(8)
    void testFailsAfterThreeAttemptsSeveredBelt() {
        var motor = new MockMotor(5, "ENGINE_PURRING", new MotorStallException("Drive belt severed."));

        verifyException(
                () -> new MotorKickstartKata().solve(motor),
                new ConveyorHaltedRuntimeException(
                        "Conveyor motor completely failed after 3 attempts.",
                        new MotorStallException("Drive belt severed.")
                ),
                verifyWrappedSameExceptionClassAndMessage
        );

        verifyClass(
                motor,
                MockMotor::getAttempts,
                3,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Safely halts the conveyor line when an electrical brownout prevents ignition after three tries.")
    @Order(9)
    void testFailsAfterThreeAttemptsBrownout() {
        var motor = new MockMotor(6, "ENGINE_PURRING", new MotorStallException("Insufficient voltage for ignition."));

        verifyException(
                () -> new MotorKickstartKata().solve(motor),
                new ConveyorHaltedRuntimeException(
                        "Conveyor motor completely failed after 3 attempts.",
                        new MotorStallException("Insufficient voltage for ignition.")
                ),
                verifyWrappedSameExceptionClassAndMessage
        );

        verifyClass(
                motor,
                MockMotor::getAttempts,
                3,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Safely halts the conveyor line when jammed coffee beans lock the rotor for three straight attempts.")
    @Order(10)
    void testFailsAfterThreeAttemptsJammedRotor() {
        var motor = new MockMotor(10, "ENGINE_PURRING", new MotorStallException("Rotor locked by debris."));

        verifyException(
                () -> new MotorKickstartKata().solve(motor),
                new ConveyorHaltedRuntimeException(
                        "Conveyor motor completely failed after 3 attempts.",
                        new MotorStallException("Rotor locked by debris.")
                ),
                verifyWrappedSameExceptionClassAndMessage
        );

        verifyClass(
                motor,
                MockMotor::getAttempts,
                3,
                Objects::equals
        );
    }
}