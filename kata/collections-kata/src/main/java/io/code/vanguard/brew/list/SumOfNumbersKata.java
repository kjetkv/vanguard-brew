package io.code.vanguard.brew.list;

import io.code.vanguard.brew.BasicKata;

import java.util.List;

public class SumOfNumbersKata implements BasicKata<List<Integer>, Integer> {

    @Override
    public Integer solve(List<Integer> input) {
        return input.stream()
                .mapToInt(Integer::intValue)
                .sum();
    }
}
