package io.code.vanguard.brew.cuncurrency;

import io.code.vanguard.brew.BasicKata;

import static io.code.vanguard.brew.cuncurrency.ManualOverrideKata.MachineTask;

public class ManualOverrideKata implements BasicKata<MachineTask, Boolean> {

    public abstract static class MachineTask implements Runnable {
        public abstract boolean isCompleted();
    }

    @Override
    public Boolean solve(MachineTask task) {
        return null;
    }
}
