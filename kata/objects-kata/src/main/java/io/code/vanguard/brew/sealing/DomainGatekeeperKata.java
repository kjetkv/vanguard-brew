package io.code.vanguard.brew.sealing;

import io.code.vanguard.brew.BasicKata;

import static io.code.vanguard.brew.sealing.DomainGatekeeperKata.Account;

public class DomainGatekeeperKata implements BasicKata<Account, String> {

    public sealed interface Account permits Guest, Member, Admin {
    }

    public record Guest(String sessionId) implements Account {
    }

    public record Member(String username, String tier) implements Account {
    }

    public record Admin(String username, int clearanceLevel) implements Account {
    }

    @Override
    public String solve(Account account) {
        return null;
    }
}