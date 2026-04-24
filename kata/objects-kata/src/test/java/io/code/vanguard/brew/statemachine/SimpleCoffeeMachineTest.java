package io.code.vanguard.brew.statemachine;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Validators.verifySameExceptionClassAndMessage;
import static io.code.vanguard.brew.statemachine.SimpleCoffeeMachineKata.Event.MAKE_COFFEE;
import static io.code.vanguard.brew.statemachine.SimpleCoffeeMachineKata.Event.POWER_OFF;
import static io.code.vanguard.brew.statemachine.SimpleCoffeeMachineKata.Event.POWER_ON;
import static io.code.vanguard.brew.statemachine.SimpleCoffeeMachineKata.Event.WARM_UP;
import static io.code.vanguard.brew.statemachine.SimpleCoffeeMachineKata.InvalidTransitionException;
import static io.code.vanguard.brew.statemachine.SimpleCoffeeMachineKata.State.BREWING;
import static io.code.vanguard.brew.statemachine.SimpleCoffeeMachineKata.State.IDLE;
import static io.code.vanguard.brew.statemachine.SimpleCoffeeMachineKata.State.OFF;
import static io.code.vanguard.brew.statemachine.SimpleCoffeeMachineKata.State.READY;
import static io.code.vanguard.brew.statemachine.SimpleCoffeeMachineKata.TransitionRequest;

@DisplayName("StateMachine - Rookie Barista")
@Tag("StateMachine")
public class SimpleCoffeeMachineTest extends BasicKataTestBase {

    @Test
    @DisplayName("When OFF and receiving POWER_ON, transitions to IDLE.")
    @Order(1)
    void testPowerOn() {
        verifyBasicKata(
                new SimpleCoffeeMachineKata(),
                new TransitionRequest(OFF, POWER_ON),
                IDLE,
                Objects::equals
        );
    }

    @Test
    @DisplayName("When IDLE and receiving WARM_UP, transitions to READY.")
    @Order(2)
    void testWarmUp() {
        verifyBasicKata(
                new SimpleCoffeeMachineKata(),
                new TransitionRequest(IDLE, WARM_UP),
                READY,
                Objects::equals
        );
    }

    @Test
    @DisplayName("When READY and receiving MAKE_COFFEE, transitions to BREWING.")
    @Order(3)
    void testMakeCoffee() {
        verifyBasicKata(
                new SimpleCoffeeMachineKata(),
                new TransitionRequest(READY, MAKE_COFFEE),
                BREWING,
                Objects::equals
        );
    }

    @Test
    @DisplayName("When IDLE and receiving POWER_OFF, gracefully shuts down to OFF.")
    @Order(4)
    void testGracefulShutdown() {
        verifyBasicKata(
                new SimpleCoffeeMachineKata(),
                new TransitionRequest(IDLE, POWER_OFF),
                OFF,
                Objects::equals
        );
    }

    @Test
    @DisplayName("When OFF and receiving MAKE_COFFEE, throws InvalidTransitionException with dynamic message.")
    @Order(5)
    void testBrewWhileOff() {
        verifyException(
                () -> new SimpleCoffeeMachineKata().solve(
                        new TransitionRequest(OFF, MAKE_COFFEE)
                ),
                new InvalidTransitionException("Invalid transition: Cannot MAKE_COFFEE while OFF"),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("When BREWING and receiving POWER_OFF, throws InvalidTransitionException with specific Danger message.")
    @Order(6)
    void testPowerOffWhileBrewing() {
        verifyException(
                () -> new SimpleCoffeeMachineKata().solve(
                        new TransitionRequest(BREWING, POWER_OFF)
                ),
                new InvalidTransitionException("Danger: Cannot turn off while brewing!"),
                verifySameExceptionClassAndMessage
        );
    }
}