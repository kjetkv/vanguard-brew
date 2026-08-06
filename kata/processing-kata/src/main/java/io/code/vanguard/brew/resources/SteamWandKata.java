package io.code.vanguard.brew.resources;

import io.code.vanguard.brew.BasicKata;

import static io.code.vanguard.brew.resources.SteamWandKata.RawSteamWand;

public class SteamWandKata implements BasicKata<RawSteamWand, AutoCloseable> {

    public static class RawSteamWand {
        private boolean pressurized = true;

        public void depressurize() {
            this.pressurized = false;
        }

        public boolean isPressurized() {
            return pressurized;
        }

        @Override
        public String toString() {
            return "RawSteamWand[Pressurized: " + pressurized + "]";
        }
    }

    @Override
    public AutoCloseable solve(RawSteamWand legacyWand) {
        return null;
    }
}