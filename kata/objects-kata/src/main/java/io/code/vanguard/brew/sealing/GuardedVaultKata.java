package io.code.vanguard.brew.sealing;

import io.code.vanguard.brew.BasicKata;

public class GuardedVaultKata implements BasicKata<GuardedVaultKata.Transaction, String> {

    public interface Transaction { }

    public static class Deposit implements Transaction {
        public Deposit(double amount) {

        }
    }

    public static class Withdrawal implements Transaction {
        public Withdrawal(double amount) {

        }
    }

    @Override
    public String solve(Transaction transaction) {
        return null;
    }
}