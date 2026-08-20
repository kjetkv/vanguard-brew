package io.code.vanguard.brew.exception;

import io.code.vanguard.brew.BasicKata;

import static io.code.vanguard.brew.exception.JammedHopperKata.LoadedHopper;

public class JammedHopperKata implements BasicKata<Integer, LoadedHopper> {

    public record LoadedHopper(int actualGrams) { }

    public static class InvalidHopperMassException extends RuntimeException {
        public InvalidHopperMassException(String message) {
            super(message);
        }
    }

    @Override
    public LoadedHopper solve(Integer requestedGrams) {
        return null;
    }
}