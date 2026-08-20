package io.code.vanguard.brew.resources;


import io.code.vanguard.brew.BasicKata;

import static io.code.vanguard.brew.resources.LegacyValveKata.LegacyValve;
import static io.code.vanguard.brew.resources.LegacyValveKata.OperationResult;

public class LegacyValveKata implements BasicKata<LegacyValve, OperationResult> {

    public static class FlowException extends Exception {
        public FlowException(String message) {
            super(message);
        }
    }

    public interface LegacyValve {
        int pullWater() throws FlowException;

        void closeValve();

        boolean isClosed();
    }

    public record OperationResult(int actualWater, boolean valveClosed) { }

    @Override
    public OperationResult solve(LegacyValve valve) {
        return null;
    }
}