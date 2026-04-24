package io.code.vanguard.brew.statemachine;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Validators.verifySameExceptionClassAndMessage;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.Event.BREW_CAPPUCCINO;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.Event.BREW_DONE;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.Event.BREW_ESPRESSO;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.Event.POWER_OFF;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.Event.POWER_ON;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.Event.RUN_CLEANING;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.Event.STEAM_DONE;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.Event.TAMP_DONE;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.Event.WARMED_UP;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.InvalidTransitionException;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.Recipe.CAPPUCCINO;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.Recipe.ESPRESSO;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.State.Brewing;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.State.Grinding;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.State.Idle;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.State.NeedsCleaning;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.State.Off;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.State.Ready;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.State.Steaming;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.State.Tamping;
import static io.code.vanguard.brew.statemachine.MasterBaristaKata.TransitionRequest;

@DisplayName("StateMachine - Master Barista")
@Tag("StateMachine")
public class MasterBaristaTest extends BasicKataTestBase {

    @Test
    @DisplayName("When OFF and receiving POWER_ON, transitions to IDLE.")
    @Order(1)
    void testBootSequence() {
        verifyBasicKata(
                new MasterBaristaKata(),
                new TransitionRequest(new Off(), POWER_ON),
                new Idle(),
                Objects::equals
        );
    }

    @Test
    @DisplayName("When IDLE and receiving WARMED_UP, transitions to READY.")
    @Order(2)
    void testWarmUpSequence() {
        verifyBasicKata(
                new MasterBaristaKata(),
                new TransitionRequest(new Idle(), WARMED_UP),
                new Ready(),
                Objects::equals
        );
    }

    @Test
    @DisplayName("When READY and BREW_ESPRESSO is pressed, starts GRINDING for ESPRESSO.")
    @Order(3)
    void testStartEspresso() {
        verifyBasicKata(
                new MasterBaristaKata(),
                new TransitionRequest(new Ready(), BREW_ESPRESSO),
                new Grinding(ESPRESSO),
                Objects::equals
        );
    }

    @Test
    @DisplayName("When READY and BREW_CAPPUCCINO is pressed, starts GRINDING for CAPPUCCINO.")
    @Order(4)
    void testStartCappuccino() {
        verifyBasicKata(
                new MasterBaristaKata(),
                new TransitionRequest(new Ready(), BREW_CAPPUCCINO),
                new Grinding(CAPPUCCINO),
                Objects::equals
        );
    }

    @Test
    @DisplayName("When BREW_DONE triggers for an ESPRESSO, returns to READY.")
    @Order(5)
    void testBrewDoneEspresso() {
        verifyBasicKata(
                new MasterBaristaKata(),
                new TransitionRequest(new Brewing(ESPRESSO), BREW_DONE),
                new Ready(),
                Objects::equals
        );
    }

    @Test
    @DisplayName("When BREW_DONE triggers for a CAPPUCCINO, proceeds to STEAMING.")
    @Order(6)
    void testBrewDoneCappuccino() {
        verifyBasicKata(
                new MasterBaristaKata(),
                new TransitionRequest(new Brewing(CAPPUCCINO), BREW_DONE),
                new Steaming(),
                Objects::equals
        );
    }

    @Test
    @DisplayName("When STEAMING is done, machine demands NEEDS_CLEANING.")
    @Order(7)
    void testSteamingDone() {
        verifyBasicKata(
                new MasterBaristaKata(),
                new TransitionRequest(new Steaming(), STEAM_DONE),
                new NeedsCleaning(),
                Objects::equals
        );
    }

    @Test
    @DisplayName("When NEEDS_CLEANING and RUN_CLEANING executes, returns to READY.")
    @Order(8)
    void testCleaningDone() {
        verifyBasicKata(
                new MasterBaristaKata(),
                new TransitionRequest(new NeedsCleaning(), RUN_CLEANING),
                new Ready(),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Cannot WARM_UP while OFF.")
    @Order(9)
    void testWarmUpWhileOff() {
        verifyException(
                () -> new MasterBaristaKata().solve(
                        new TransitionRequest(new Off(), WARMED_UP)
                ),
                new InvalidTransitionException("Invalid transition: Cannot WARMED_UP while in state Off"),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Cannot brew while IDLE.")
    @Order(10)
    void testBrewWhileIdle() {
        verifyException(
                () -> new MasterBaristaKata().solve(
                        new TransitionRequest(new Idle(), BREW_ESPRESSO)
                ),
                new InvalidTransitionException("Invalid transition: Cannot BREW_ESPRESSO while in state Idle"),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Cannot brew if the machine NEEDS_CLEANING.")
    @Order(11)
    void testBrewWhileDirty() {
        verifyException(
                () -> new MasterBaristaKata().solve(
                        new TransitionRequest(new NeedsCleaning(), BREW_CAPPUCCINO)
                ),
                new InvalidTransitionException("Invalid transition: Cannot BREW_CAPPUCCINO while in state NeedsCleaning"),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Cannot trigger TAMP_DONE while still GRINDING.")
    @Order(12)
    void testTampDoneWhileGrinding() {
        verifyException(
                () -> new MasterBaristaKata().solve(
                        new TransitionRequest(new Grinding(ESPRESSO), TAMP_DONE)
                ),
                new InvalidTransitionException("Invalid transition: Cannot TAMP_DONE while in state Grinding"),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Cannot press BREW_CAPPUCCINO while GRINDING an ESPRESSO.")
    @Order(13)
    void testMashButtonsWhileBrewing() {
        verifyException(
                () -> new MasterBaristaKata().solve(
                        new TransitionRequest(new Grinding(ESPRESSO), BREW_CAPPUCCINO)
                ),
                new InvalidTransitionException("Invalid transition: Cannot BREW_CAPPUCCINO while in state Grinding"),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Attempting to POWER_OFF while GRINDING throws specific Danger exception.")
    @Order(14)
    void testPowerOffWhileGrinding() {
        verifyException(
                () -> new MasterBaristaKata().solve(
                        new TransitionRequest(new Grinding(ESPRESSO), POWER_OFF)
                ),
                new InvalidTransitionException("Danger: Cannot power off while grinder is active!"),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Attempting to POWER_OFF while TAMPING throws specific Danger exception.")
    @Order(15)
    void testPowerOffWhileTamping() {
        verifyException(
                () -> new MasterBaristaKata().solve(
                        new TransitionRequest(new Tamping(CAPPUCCINO), POWER_OFF)
                ),
                new InvalidTransitionException("Danger: Cannot power off while tamping mechanism is engaged!"),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Attempting to POWER_OFF while BREWING throws specific Danger exception.")
    @Order(16)
    void testPowerOffWhilePressurized() {
        verifyException(
                () -> new MasterBaristaKata().solve(
                        new TransitionRequest(new Brewing(CAPPUCCINO), POWER_OFF)
                ),
                new InvalidTransitionException("Danger: Cannot power off while system is pressurized!"),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Attempting to POWER_OFF while STEAMING throws specific Danger exception.")
    @Order(17)
    void testPowerOffWhileSteaming() {
        verifyException(
                () -> new MasterBaristaKata().solve(
                        new TransitionRequest(new Steaming(), POWER_OFF)
                ),
                new InvalidTransitionException("Danger: Cannot power off while steam wand is hot!"),
                verifySameExceptionClassAndMessage
        );
    }
}
