package io.code.vanguard.brew.cuncurrency;

import io.code.vanguard.brew.BasicKata;

import java.util.List;

import static io.code.vanguard.brew.cuncurrency.DispatcherKata.Workload;

public class DispatcherKata implements BasicKata<Workload, List<String>> {

    public interface PackingMachine {
        String pack(String orderId);
    }

    public record Workload(List<String> orders, PackingMachine machine) { }

    @Override
    public List<String> solve(Workload workload) {
        return null;
    }
}