package io.code.vanguard.brew.resources;


import io.code.vanguard.brew.BasicKata;

import java.util.function.Function;

import static io.code.vanguard.brew.resources.BlastProofBaristaBoxKata.EspressoOperation;
import static io.code.vanguard.brew.resources.BlastProofBaristaBoxKata.FoamedMilk;

public class BlastProofBaristaBoxKata implements BasicKata<EspressoOperation, FoamedMilk> {

    public record FoamedMilk(int grams) { }

    public static class SafeSteamWand implements AutoCloseable {
        private boolean pressurized = true;

        public void injectSteam(int massInGrams) {
            if (!pressurized) {
                throw new IllegalStateException("Cannot inject steam: wand is depressurized.");
            }
        }

        public boolean isPressurized() {
            return pressurized;
        }

        @Override
        public void close() {
            this.pressurized = false;
        }
    }

    public record EspressoOperation(SafeSteamWand wand, Function<SafeSteamWand, FoamedMilk> baristaAction) { }

    @Override
    public FoamedMilk solve(EspressoOperation operation) {
        return null;
    }
}
