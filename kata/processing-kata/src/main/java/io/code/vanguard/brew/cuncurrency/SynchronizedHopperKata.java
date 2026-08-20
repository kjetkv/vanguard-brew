package io.code.vanguard.brew.cuncurrency;

import io.code.vanguard.brew.BasicKata;

import java.util.List;

import static io.code.vanguard.brew.cuncurrency.SynchronizedHopperKata.BeanSack;

public class SynchronizedHopperKata implements BasicKata<List<BeanSack>, Integer> {

    public interface BeanSack {
        int pour();
    }

    @Override
    public Integer solve(List<BeanSack> batch) {
        return null;
    }
}
