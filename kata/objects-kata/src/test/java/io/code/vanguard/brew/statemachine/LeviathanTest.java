package io.code.vanguard.brew.statemachine;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Validators.verifySameExceptionClassAndMessage;
import static io.code.vanguard.brew.statemachine.LeviathanKata.BrewState.BrewerIdle;
import static io.code.vanguard.brew.statemachine.LeviathanKata.BrewState.Brewing;
import static io.code.vanguard.brew.statemachine.LeviathanKata.BrewState.Grinding;
import static io.code.vanguard.brew.statemachine.LeviathanKata.Event.BREW_CMD;
import static io.code.vanguard.brew.statemachine.LeviathanKata.Event.BREW_DONE;
import static io.code.vanguard.brew.statemachine.LeviathanKata.Event.E_STOP;
import static io.code.vanguard.brew.statemachine.LeviathanKata.Event.GRIND_DONE;
import static io.code.vanguard.brew.statemachine.LeviathanKata.Event.POWER_OFF;
import static io.code.vanguard.brew.statemachine.LeviathanKata.Event.POWER_ON;
import static io.code.vanguard.brew.statemachine.LeviathanKata.Event.STEAM_CMD;
import static io.code.vanguard.brew.statemachine.LeviathanKata.Event.STEAM_DONE;
import static io.code.vanguard.brew.statemachine.LeviathanKata.InsufficientResourcesException;
import static io.code.vanguard.brew.statemachine.LeviathanKata.InvalidTransitionException;
import static io.code.vanguard.brew.statemachine.LeviathanKata.Inventory;
import static io.code.vanguard.brew.statemachine.LeviathanKata.State.Active;
import static io.code.vanguard.brew.statemachine.LeviathanKata.State.Off;
import static io.code.vanguard.brew.statemachine.LeviathanKata.SteamState.SteamerIdle;
import static io.code.vanguard.brew.statemachine.LeviathanKata.SteamState.Steaming;
import static io.code.vanguard.brew.statemachine.LeviathanKata.TransitionRequest;

@DisplayName("StateMachine - The Leviathan")
@Tag("StateMachine")
public class LeviathanTest extends BasicKataTestBase {

    private final Inventory fullHopper = new Inventory(1000, 2000);
    private final Inventory lowBeans = new Inventory(15, 2000);
    private final Inventory lowWater = new Inventory(1000, 40);

    @Test
    @DisplayName("POWER_ON brings the machine from Off to Active(Idle, Idle).")
    @Order(1)
    void testBootSequence() {
        verifyBasicKata(
                new LeviathanKata(),
                new TransitionRequest(new Off(), POWER_ON, fullHopper),
                new Active(new BrewerIdle(), new SteamerIdle()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("POWER_OFF safely turns the machine Off if both subsystems are Idle.")
    @Order(2)
    void testSafeShutdown() {
        verifyBasicKata(
                new LeviathanKata(),
                new TransitionRequest(
                        new Active(new BrewerIdle(), new SteamerIdle()),
                        POWER_OFF,
                        fullHopper),
                new Off(),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Attempting to POWER_ON while already Active throws InvalidTransition.")
    @Order(3)
    void testPowerOnWhileActive() {
        verifyException(
                () -> new LeviathanKata().solve(
                        new TransitionRequest(
                                new Active(new BrewerIdle(), new SteamerIdle()),
                                POWER_ON,
                                fullHopper)
                ),
                new InvalidTransitionException("Invalid transition: Cannot POWER_ON in current state."),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("BREW_CMD starts Grinding while leaving Steam Idle.")
    @Order(4)
    void testBrewStartsGrinding() {
        verifyBasicKata(
                new LeviathanKata(),
                new TransitionRequest(
                        new Active(new BrewerIdle(), new SteamerIdle()),
                        BREW_CMD,
                        fullHopper),
                new Active(new Grinding(), new SteamerIdle()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("GRIND_DONE transitions to Brewing while leaving Steam Idle.")
    @Order(5)
    void testGrindDoneTransitionsToBrewing() {
        verifyBasicKata(
                new LeviathanKata(),
                new TransitionRequest(
                        new Active(new Grinding(), new SteamerIdle()),
                        GRIND_DONE,
                        fullHopper),
                new Active(new Brewing(), new SteamerIdle()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("BREW_DONE returns BrewState to Idle.")
    @Order(6)
    void testBrewDoneReturnsToIdle() {
        verifyBasicKata(
                new LeviathanKata(),
                new TransitionRequest(
                        new Active(new Brewing(), new SteamerIdle()),
                        BREW_DONE,
                        fullHopper),
                new Active(new BrewerIdle(), new SteamerIdle()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Can start Steaming while the grinder is running.")
    @Order(7)
    void testSteamWhileGrinding() {
        verifyBasicKata(
                new LeviathanKata(),
                new TransitionRequest(
                        new Active(new Grinding(), new SteamerIdle()),
                        STEAM_CMD,
                        fullHopper),
                new Active(new Grinding(), new Steaming()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Grinder finishing does not interrupt active Steaming.")
    @Order(8)
    void testGrindDoneWhileSteaming() {
        verifyBasicKata(
                new LeviathanKata(),
                new TransitionRequest(
                        new Active(new Grinding(), new Steaming()),
                        GRIND_DONE,
                        fullHopper),
                new Active(new Brewing(), new Steaming()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Can initiate a Brew cycle while already Steaming.")
    @Order(9)
    void testBrewWhileSteaming() {
        verifyBasicKata(
                new LeviathanKata(),
                new TransitionRequest(
                        new Active(new BrewerIdle(), new Steaming()),
                        BREW_CMD,
                        fullHopper),
                new Active(new Grinding(), new Steaming()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Steaming finishing does not interrupt active Brewing.")
    @Order(10)
    void testSteamDoneWhileBrewing() {
        verifyBasicKata(
                new LeviathanKata(),
                new TransitionRequest(
                        new Active(new Brewing(), new Steaming()),
                        STEAM_DONE,
                        fullHopper),
                new Active(new Brewing(), new SteamerIdle()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Cannot Brew if Beans are below 18g.")
    @Order(11)
    void testLowBeansPreventsBrew() {
        verifyException(
                () -> new LeviathanKata().solve(
                        new TransitionRequest(
                                new Active(new BrewerIdle(), new SteamerIdle()),
                                BREW_CMD,
                                lowBeans)
                ),
                new InsufficientResourcesException("Hopper empty! Need at least 18g beans and 50ml water."),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Cannot Brew if Water is below 50ml.")
    @Order(12)
    void testLowWaterPreventsBrew() {
        verifyException(
                () -> new LeviathanKata().solve(
                        new TransitionRequest(
                                new Active(new BrewerIdle(), new SteamerIdle()),
                                BREW_CMD,
                                lowWater)
                ),
                new InsufficientResourcesException("Hopper empty! Need at least 18g beans and 50ml water."),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Normal POWER_OFF fails if Steam is active.")
    @Order(13)
    void testPowerOffFailsWhileSteaming() {
        verifyException(
                () -> new LeviathanKata().solve(
                        new TransitionRequest(
                                new Active(new BrewerIdle(), new Steaming()),
                                POWER_OFF,
                                fullHopper)
                ),
                new InvalidTransitionException("Danger: Cannot power off safely while subsystems are active!"),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Skipping steps (e.g. BREW_DONE while Idle) throws InvalidTransition.")
    @Order(14)
    void testSkipStepsThrowsException() {
        verifyException(
                () -> new LeviathanKata().solve(
                        new TransitionRequest(
                                new Active(new BrewerIdle(), new SteamerIdle()),
                                BREW_DONE,
                                fullHopper)
                ),
                new InvalidTransitionException("Invalid transition: Cannot BREW_DONE in current state."),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("E_STOP immediately kills the machine even during max-load dual processing.")
    @Order(15)
    void testEmergencyStopKillsDualProcessing() {
        verifyBasicKata(
                new LeviathanKata(),
                new TransitionRequest(
                        new Active(new Brewing(), new Steaming()),
                        E_STOP,
                        fullHopper),
                new Off(),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Cannot start a new brew while already Grinding (No Double Brewing).")
    @Order(16)
    void testNoDoubleBrewing() {
        verifyException(
                () -> new LeviathanKata().solve(
                        new TransitionRequest(
                                new Active(new Grinding(), new SteamerIdle()),
                                BREW_CMD,
                                fullHopper)
                ),
                new InvalidTransitionException("Invalid transition: Cannot BREW_CMD in current state."),
                verifySameExceptionClassAndMessage
        );
    }
}