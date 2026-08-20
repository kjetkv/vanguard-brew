package io.code.vanguard.brew.functional;

import io.code.vanguard.brew.BasicKata;

import java.util.Objects;
import java.util.function.Consumer;

import static io.code.vanguard.brew.functional.MasterAlchemistKata.BrewedCoffee;
import static io.code.vanguard.brew.functional.MasterAlchemistKata.Machinery;

public class MasterAlchemistKata implements BasicKata<Consumer<BrewedCoffee>, Machinery> {

    public record RawOrder(String orderId, String recipeName, int requestedGrams) { }

    public record IngredientBatch(String recipeName, int beanGrams, int waterGrams) { }

    public record BrewedCoffee(String recipeName, int actualGrams, boolean passedInspection) { }

    public record Machinery(Consumer<RawOrder> masterPipeline) {
        @Override
        public String toString() {
            return "Machinery{" +
                    "masterPipeline=" + (Objects.isNull(masterPipeline) ? "not installed" : "installed") +
                    '}';
        }
    }

    @Override
    public Machinery solve(Consumer<BrewedCoffee> dispatchLine) {
        return null;
    }
}