package io.code.vanguard.brew.resources;

import io.code.vanguard.brew.BasicKataTestBase;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Validators.verifySameExceptionClassAndMessage;
import static io.code.vanguard.brew.resources.BlastProofBaristaBoxKata.EspressoOperation;
import static io.code.vanguard.brew.resources.BlastProofBaristaBoxKata.FoamedMilk;
import static io.code.vanguard.brew.resources.BlastProofBaristaBoxKata.SafeSteamWand;
import static java.util.Optional.ofNullable;

@DisplayName("Resources - Blast-Proof Barista Box")
@Tag("Resiliency")
@Tag("Resources")
public class BlastProofBaristaBoxTest extends BasicKataTestBase {

    @Test
    @DisplayName("Guarantees the certified wand initializes in a fully pressurized, dangerous state.")
    @Order(1)
    void testWandStartsPressurized() {
        var wand = new SafeSteamWand();

        verifyClass(
                wand,
                SafeSteamWand::isPressurized,
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully yields a standard 250g milk foaming operation and safely depressurizes.")
    @Order(2)
    void testStandardOperationYieldsResultAndCloses() {
        var kata = new BlastProofBaristaBoxKata();
        var wand = new SafeSteamWand();
        var operation = new EspressoOperation(wand, activeWand -> {
            activeWand.injectSteam(250);
            return new FoamedMilk(250);
        });

        verifyFunctionalKata(
                kata,
                operation,
                "Yield: 250g | Pressurized: false",
                result -> formatResult(result, wand),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully yields a massive 1000g industrial foaming operation and safely depressurizes.")
    @Order(3)
    void testIndustrialOperationYieldsResultAndCloses() {
        var kata = new BlastProofBaristaBoxKata();
        var wand = new SafeSteamWand();
        var operation = new EspressoOperation(wand, activeWand -> {
            activeWand.injectSteam(1000);
            return new FoamedMilk(1000);
        });

        verifyFunctionalKata(
                kata,
                operation,
                "Yield: 1000g | Pressurized: false",
                result -> formatResult(result, wand),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully handles a complex sequential steam injection and returns total mass.")
    @Order(4)
    void testSequentialInjectionYieldsResultAndCloses() {
        var kata = new BlastProofBaristaBoxKata();
        var wand = new SafeSteamWand();
        var operation = new EspressoOperation(wand, activeWand -> {
            activeWand.injectSteam(100);
            activeWand.injectSteam(50);
            return new FoamedMilk(150);
        });

        verifyFunctionalKata(
                kata,
                operation,
                "Yield: 150g | Pressurized: false",
                result -> formatResult(result, wand),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Safely intercepts a barista dropping the pitcher during injection and triggers automatic depressurization.")
    @Order(5)
    void testClosesOnRuntimeException() {
        var wand = new SafeSteamWand();
        var operation = new EspressoOperation(wand, activeWand -> {
            activeWand.injectSteam(50);
            throw new RuntimeException("Barista dropped the pitcher!");
        });

        verifyException(
                () -> new BlastProofBaristaBoxKata().solve(operation),
                new RuntimeException("Barista dropped the pitcher!"),
                verifySameExceptionClassAndMessage
        );

        verifyClass(
                wand,
                SafeSteamWand::isPressurized,
                false,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Safely intercepts a physical pressure loss error and triggers automatic depressurization.")
    @Order(6)
    void testClosesOnIllegalStateException() {
        var wand = new SafeSteamWand();
        var operation = new EspressoOperation(wand, _ -> {
            throw new IllegalStateException("Physical water line severed.");
        });

        verifyException(
                () -> new BlastProofBaristaBoxKata().solve(operation),
                new IllegalStateException("Physical water line severed."),
                verifySameExceptionClassAndMessage
        );

        verifyClass(
                wand,
                SafeSteamWand::isPressurized,
                false,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Safely intercepts missing milk carton logic and triggers automatic depressurization.")
    @Order(7)
    void testClosesOnNullPointerException() {
        var wand = new SafeSteamWand();
        var operation = new EspressoOperation(wand, _ -> {
            throw new NullPointerException("Milk carton data missing.");
        });

        verifyException(
                () -> new BlastProofBaristaBoxKata().solve(operation),
                new NullPointerException("Milk carton data missing."),
                verifySameExceptionClassAndMessage
        );

        verifyClass(
                wand,
                SafeSteamWand::isPressurized,
                false,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Safely intercepts a failed mathematical temperature calculation and triggers automatic depressurization.")
    @Order(8)
    void testClosesOnArithmeticException() {
        var wand = new SafeSteamWand();
        var operation = new EspressoOperation(wand, activeWand -> {
            int targetGrams = 100 / 0;
            activeWand.injectSteam(targetGrams);
            return new FoamedMilk(targetGrams);
        });

        verifyException(
                () -> new BlastProofBaristaBoxKata().solve(operation),
                new ArithmeticException("/ by zero"),
                verifySameExceptionClassAndMessage
        );

        verifyClass(
                wand,
                SafeSteamWand::isPressurized,
                false,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Guarantees hardware depressurization even during a catastrophic system blackout.")
    @Order(9)
    void testClosesOnSystemError() {
        var wand = new SafeSteamWand();
        var operation = new EspressoOperation(wand, activeWand -> {
            throw new OutOfMemoryError("Terminal heap space exceeded.");
        });

        verifyException(
                () -> new BlastProofBaristaBoxKata().solve(operation),
                new OutOfMemoryError("Terminal heap space exceeded."),
                verifySameExceptionClassAndMessage
        );

        verifyClass(
                wand,
                SafeSteamWand::isPressurized,
                false,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Safely processes an empty foaming action resulting in 0g yield and securely depressurizes.")
    @Order(10)
    void testEmptyYieldClosesWand() {
        var kata = new BlastProofBaristaBoxKata();
        var wand = new SafeSteamWand();
        var operation = new EspressoOperation(wand, activeWand -> new FoamedMilk(0));

        verifyFunctionalKata(
                kata,
                operation,
                "Yield: 0g | Pressurized: false",
                result -> formatResult(result, wand),
                Objects::equals
        );
    }

    private static @NonNull String formatResult(FoamedMilk result, SafeSteamWand wand) {
        return "Yield: %dg | Pressurized: %s"
                .formatted(
                        ofNullable(result).map(FoamedMilk::grams).orElse(0),
                        wand.isPressurized()
                );
    }
}
