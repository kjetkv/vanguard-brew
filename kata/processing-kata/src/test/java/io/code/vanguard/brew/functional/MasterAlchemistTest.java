package io.code.vanguard.brew.functional;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static io.code.vanguard.brew.functional.MasterAlchemistKata.BrewedCoffee;
import static io.code.vanguard.brew.functional.MasterAlchemistKata.Machinery;
import static io.code.vanguard.brew.functional.MasterAlchemistKata.RawOrder;

@DisplayName("Functional - Master Alchemist Factory")
@Tag("Functional")
public class MasterAlchemistTest extends BasicKataTestBase {

    @Test
    @DisplayName("Successfully brews an Espresso, applying the fixed 20/30g ratio and standard 10% mass loss.")
    @Order(1)
    void testEspressoPipeline() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-01",
                        "Espresso",
                        1000),
                "DISPATCHED: Espresso (45g)"
        );
    }

    @Test
    @DisplayName("Successfully brews a Pour Over, applying the 10/90 ratio and standard 10% mass loss.")
    @Order(2)
    void testPourOverPipeline() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-02",
                        "Pour Over",
                        1000),
                "DISPATCHED: Pour Over (900g)"
        );
    }

    @Test
    @DisplayName("Successfully brews a Cold Brew, applying the 20/80 ratio and high 15% mass loss.")
    @Order(3)
    void testColdBrewPipeline() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-03",
                        "Cold Brew",
                        1000),
                "DISPATCHED: Cold Brew (850g)"
        );
    }

    @Test
    @DisplayName("Successfully brews a French Press, applying the 15/85 ratio and efficient 5% mass loss.")
    @Order(4)
    void testFrenchPressPipeline() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-04",
                        "French Press",
                        1000),
                "DISPATCHED: French Press (950g)"
        );
    }

    @Test
    @DisplayName("Successfully brews an Aeropress, applying the strict 250g equipment cap and 8% mass loss.")
    @Order(5)
    void testAeropressPipeline() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-05",
                        "Aeropress",
                        500),
                "DISPATCHED: Aeropress (230g)"
        );
    }

    @Test
    @DisplayName("Safely dispenses nothing when an order falls below the 50g minimum threshold.")
    @Order(6)
    void testValidationMinimumLimit() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-06",
                        "Pour Over",
                        30),
                ""
                );
    }

    @Test
    @DisplayName("Safely dispenses nothing when an industrial order exceeds the 2000g maximum threshold.")
    @Order(7)
    void testValidationMaximumLimit() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-07",
                        "Cold Brew",
                        5000),
                ""
                );
    }

    @Test
    @DisplayName("Securely flushes unknown recipes without crashing the factory floor.")
    @Order(8)
    void testUnknownRecipe() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-08",
                        "Magic Potion",
                        1000),
                ""
                );
    }

    @Test
    @DisplayName("Steeps a small 85g cold brew concentrate from a 100g request.")
    @Order(9)
    void testColdBrewSmallOrder() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-C1",
                        "Cold Brew",
                        100),
                "DISPATCHED: Cold Brew (85g)"
        );
    }

    @Test
    @DisplayName("Steeps a standard 340g cold brew batch from a 400g request.")
    @Order(10)
    void testColdBrewStandardOrder() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-C2",
                        "Cold Brew",
                        400),
                "DISPATCHED: Cold Brew (340g)"
        );
    }

    @Test
    @DisplayName("Successfully processes a large 1000g cold brew, accounting for 15% liquid loss in grounds.")
    @Order(11)
    void testColdBrewLargeOrder() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-C3",
                        "Cold Brew",
                        1000),
                "DISPATCHED: Cold Brew (850g)"
        );
    }

    @Test
    @DisplayName("Maxes out the cold brew vats with a 2000g order, yielding 1700g.")
    @Order(12)
    void testColdBrewMaximumOrder() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-C4",
                        "Cold Brew",
                        2000),
                "DISPATCHED: Cold Brew (1700g)"
        );
    }

    @Test
    @DisplayName("Plunges a small 95g french press from a 100g request.")
    @Order(13)
    void testFrenchPressSmallOrder() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-F1",
                        "French Press",
                        100),
                "DISPATCHED: French Press (95g)"
        );
    }

    @Test
    @DisplayName("Plunges a standard 285g french press from a 300g request.")
    @Order(14)
    void testFrenchPressStandardOrder() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-F2",
                        "French Press",
                        300),
                "DISPATCHED: French Press (285g)"
        );
    }

    @Test
    @DisplayName("Maintains high yield efficiency on a large 1000g french press batch.")
    @Order(15)
    void testFrenchPressLargeOrder() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-F3",
                        "French Press",
                        1000),
                "DISPATCHED: French Press (950g)"
        );
    }

    @Test
    @DisplayName("Safely plunges a massive industrial 2000g french press, yielding 1900g.")
    @Order(16)
    void testFrenchPressMaximumOrder() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-F4",
                        "French Press",
                        2000),
                "DISPATCHED: French Press (1900g)"
        );
    }

    @Test
    @DisplayName("Presses a tiny 46g Aeropress shot from the absolute minimum 50g factory order.")
    @Order(17)
    void testAeropressMinimumOrder() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-A1",
                        "Aeropress",
                        50),
                "DISPATCHED: Aeropress (46g)"
        );
    }

    @Test
    @DisplayName("Presses a standard 138g Aeropress from a 150g request without hitting equipment limits.")
    @Order(18)
    void testAeropressStandardOrder() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-A2",
                        "Aeropress",
                        150),
                "DISPATCHED: Aeropress (138g)"
        );
    }

    @Test
    @DisplayName("Hits the exact physical ceiling of the Aeropress chamber on a 250g request.")
    @Order(19)
    void testAeropressCeilingOrder() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-A3",
                        "Aeropress",
                        250),
                "DISPATCHED: Aeropress (230g)"
        );
    }

    @Test
    @DisplayName("Protects the physical equipment from overflowing by capping a 2000g request at 250g.")
    @Order(20)
    void testAeropressIndustrialOrderCap() {
        verifyMasterPipeline(
                new RawOrder(
                        "ORD-A4",
                        "Aeropress",
                        2000),
                "DISPATCHED: Aeropress (230g)"
        );
    }

    private void verifyMasterPipeline(RawOrder order, String expected) {
        AtomicReference<String> result = new AtomicReference<>(null);

        Consumer<BrewedCoffee> dispatchLine = brew ->
                result.set(Optional.ofNullable(brew)
                        .map(brewedCoffee ->
                                String.format(
                                        "DISPATCHED: %s (%dg)",
                                        brewedCoffee.recipeName(),
                                        brewedCoffee.actualGrams()))
                        .orElse(""));

        verifyFunctionalKata(
                new MasterAlchemistKata(),
                dispatchLine,
                expected,
                machinery -> {
                    Optional.ofNullable(machinery)
                            .map(Machinery::masterPipeline)
                            .ifPresent(pipeline -> pipeline.accept(order));
                    return result.get();
                },
                Objects::equals
        );
    }
}