package io.code.vanguard.brew.resources;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

@SuppressWarnings("checkstyle:EmptyBlock")
@DisplayName("Resources - Steam Wand")
@Tag("Resiliency")
@Tag("Resources")
public class SteamWandTest extends BasicKataTestBase {

    @Test
    @DisplayName("Automated safety manifold successfully pulls the legacy lever after standard milk foaming.")
    @Order(2)
    void testAutomatedSafetyNormalExecution() {
        var legacyWand = new SteamWandKata.RawSteamWand();

        verifyFunctionalKata(
                new SteamWandKata(),
                legacyWand,
                false,
                wrapper -> {
                    try (var _ = wrapper) {
                        // Foaming milk...
                    } catch (Exception _) {
                    }
                    return legacyWand.isPressurized();
                },
                Objects::equals
        );
    }


    @Test
    @DisplayName("Manually triggering the adapter securely depressurizes the underlying legacy wand.")
    @Order(1)
    void testManualCloseDepressurizes() {
        var legacyWand = new SteamWandKata.RawSteamWand();

        verifyFunctionalKata(
                new SteamWandKata(),
                legacyWand,
                false, // Expected: Wand is no longer pressurized
                wrapper -> {
                    try {
                        wrapper.close();
                    } catch (Exception e) {
                        // Suppress checked exception from AutoCloseable signature
                    }
                    return legacyWand.isPressurized();
                },
                Objects::equals
        );
    }

    @Test
    @DisplayName("Adapter translates unexpected barista pitcher drop into an immediate mechanical shutdown.")
    @Order(3)
    void testAutomatedSafetyWithUnexpectedRupture() {
        var legacyWand = new SteamWandKata.RawSteamWand();

        verifyFunctionalKata(
                new SteamWandKata(),
                legacyWand,
                false,
                wrapper -> {
                    try (var activeManifold = wrapper) {
                        throw new RuntimeException("Milk pitcher dropped!");
                    } catch (Exception _) {
                    }
                    return legacyWand.isPressurized();
                },
                Objects::equals
        );
    }

    @Test
    @DisplayName("Adapter translates a checked hardware fault into an immediate mechanical shutdown.")
    @Order(4)
    void testAutomatedSafetyWithHardwareFault() {
        var legacyWand = new SteamWandKata.RawSteamWand();

        verifyFunctionalKata(
                new SteamWandKata(),
                legacyWand,
                false,
                wrapper -> {
                    try (var activeManifold = wrapper) {
                        throw new Exception("Sensor failure detected.");
                    } catch (Exception _) {
                    }
                    return legacyWand.isPressurized();
                },
                Objects::equals
        );
    }

    @Test
    @DisplayName("Adapter guarantees legacy hardware shutdown even during a catastrophic main terminal blackout.")
    @Order(5)
    void testAutomatedSafetyWithSystemBlackout() {
        var legacyWand = new SteamWandKata.RawSteamWand();

        verifyFunctionalKata(
                new SteamWandKata(),
                legacyWand,
                false,
                wrapper -> {
                    try (var activeManifold = wrapper) {
                        throw new OutOfMemoryError("Factory terminal memory exceeded.");
                    } catch (Error | Exception _) {
                    }
                    return legacyWand.isPressurized();
                },
                Objects::equals
        );
    }

    @Test
    @DisplayName("Adapter safely ignores redundant closure requests from panic-spamming the emergency stop.")
    @Order(6)
    void testIdempotentClosure() {
        var legacyWand = new SteamWandKata.RawSteamWand();

        verifyFunctionalKata(
                new SteamWandKata(),
                legacyWand,
                false,
                wrapper -> {
                    try {
                        wrapper.close();
                        wrapper.close();
                        wrapper.close();
                    } catch (Exception e) {
                    }
                    return legacyWand.isPressurized();
                },
                Objects::equals
        );
    }

    @Test
    @DisplayName("Dual-adapted wands running on a single manifold both safely depressurize simultaneously.")
    @Order(7)
    void testMultipleAdaptersSimultaneousOperation() {
        var legacyWand1 = new SteamWandKata.RawSteamWand();
        var legacyWand2 = new SteamWandKata.RawSteamWand();

        var kata = new SteamWandKata();
        var wrapper1 = kata.solve(legacyWand1);
        var wrapper2 = kata.solve(legacyWand2);

        verifyClass(
                kata,
                k -> {
                    try (var w1 = wrapper1; var w2 = wrapper2) {
                        // Simultaneous operation
                    } catch (Exception e) {
                    }
                },
                k -> legacyWand1.isPressurized() + ", " + legacyWand2.isPressurized(),
                "false, false",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Depressurization engages flawlessly even when adapter is deeply nested in failing subsystem logic.")
    @Order(8)
    void testNestedSubsystemShutdown() {
        var legacyWand = new SteamWandKata.RawSteamWand();

        verifyFunctionalKata(
                new SteamWandKata(),
                legacyWand,
                false,
                wrapper -> {
                    try {
                        try (var activeManifold = wrapper) {
                            throw new IllegalArgumentException("Incorrect milk temperature profile.");
                        }
                    } catch (Exception e) {
                        // Caught by outer system
                    }
                    return legacyWand.isPressurized();
                },
                Objects::equals
        );
    }
}