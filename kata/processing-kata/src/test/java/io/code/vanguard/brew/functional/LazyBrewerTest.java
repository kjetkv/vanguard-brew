package io.code.vanguard.brew.functional;

import io.code.vanguard.brew.BasicKataTestBase;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static io.code.vanguard.brew.functional.LazyBrewerKata.ColdBrewBottle;
import static io.code.vanguard.brew.functional.LazyBrewerKata.Machinery;

@DisplayName("Functional - Lazy Brewer")
@Tag("Functional")
public class LazyBrewerTest extends BasicKataTestBase {

    @Test
    @DisplayName("Successfully brews a fresh 1000g batch when requested.")
    @Order(1)
    void testSupplierProducesCorrectBottle() {
        verifyFunctionalKata(
                new LazyBrewerKata(),
                "CB-VANGUARD-01",
                "ColdBrewBottle{Batch=CB-VANGUARD-01, Weight=1000, Sealed=false, Labeled=false}",
                machinery ->
                        Optional.ofNullable(machinery)
                                .map(Machinery::brewer)
                                .map(Supplier::get)
                                .map(Objects::toString)
                                .orElse(null),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Accurately applies a tamper-proof seal to the physical bottle.")
    @Order(2)
    void testSealerConsumer() {
        verifyFunctionalKata(
                new LazyBrewerKata(),
                "CB-VANGUARD-01",
                "ColdBrewBottle{Batch=CB-VANGUARD-01, Weight=1000, Sealed=true, Labeled=false}",
                machinery -> useMachinery(machinery, machinery.sealer()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Accurately applies a shipping label to the physical bottle.")
    @Order(3)
    void testLabelerConsumer() {
        verifyFunctionalKata(
                new LazyBrewerKata(),
                "CB-VANGUARD-01",
                "ColdBrewBottle{Batch=CB-VANGUARD-01, Weight=1000, Sealed=false, Labeled=true}",
                machinery -> useMachinery(machinery, machinery.labeler()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully processes a bottle through the entire packaging line.")
    @Order(4)
    void testMasterPackagingLine() {
        verifyFunctionalKata(
                new LazyBrewerKata(),
                "CB-VANGUARD-01",
                "ColdBrewBottle{Batch=CB-VANGUARD-01, Weight=1000, Sealed=true, Labeled=true}",
                machinery -> useMachinery(machinery, machinery.packagingLine()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Ensures the brewing machinery mints a brand-new physical bottle for every request.")
    @Order(5)
    void testSupplierFreshness() {
        verifyFunctionalKata(
                new LazyBrewerKata(),
                "CB-VANGUARD-01",
                false,
                machinery -> Optional.ofNullable(machinery)
                        .map(Machinery::brewer)
                        .map(brewer -> Objects.equals(brewer.get(), brewer.get()))
                        .orElse(null),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Audits the assembly line to ensure bottles are securely sealed before shipping labels are applied.")
    @Order(6)
    void testPackagingLineSequence() {
        verifyFunctionalKata(
                new LazyBrewerKata(),
                "CB-VANGUARD-01",
                "[BREWED, SEALED, LABELED]",
                machinery -> Optional.ofNullable(machinery)
                        .map(Machinery::brewer)
                        .map(Supplier::get)
                        .map(bottle -> runMachine(machinery.packagingLine(), bottle))
                        .map(ColdBrewBottle::getAuditLog)
                        .map(Object::toString)
                        .orElse(null),
                Objects::equals
        );
    }

    private @Nullable String useMachinery(Machinery machinery, Consumer<ColdBrewBottle> machine) {
        return Optional.ofNullable(machinery)
                .map(Machinery::brewer)
                .map(Supplier::get)
                .map(bottle -> runMachine(machine, bottle))
                .map(Objects::toString)
                .orElse(null);
    }

    private ColdBrewBottle runMachine(Consumer<ColdBrewBottle> machine, ColdBrewBottle bottle) {
        Optional.ofNullable(machine)
                .ifPresent(bm -> bm.accept(bottle));
        return bottle;
    }
}