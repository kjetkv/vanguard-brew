package io.code.vanguard.brew.cuncurrency;

import io.code.vanguard.brew.BasicKata;

import static io.code.vanguard.brew.cuncurrency.DualBoilerKata.DualBoilerMachine;
import static io.code.vanguard.brew.cuncurrency.DualBoilerKata.Latte;

public class DualBoilerKata implements BasicKata<DualBoilerMachine, Latte> {

    public interface DualBoilerMachine {
        String extractEspresso();

        String steamMilk();
    }

    public record Latte(String espresso, String milk) { }

    @Override
    public Latte solve(DualBoilerMachine machine) {
        return null;
    }
}