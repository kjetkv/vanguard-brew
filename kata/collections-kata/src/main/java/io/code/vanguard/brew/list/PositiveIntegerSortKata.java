package io.code.vanguard.brew.list;

import io.code.vanguard.brew.BasicKata;

import java.util.List;

public class PositiveIntegerSortKata implements BasicKata<List<Integer>, List<Integer>> {

    @Override
    public List<Integer> solve(List<Integer> input) {
        return input.stream()
                .filter(i -> i > 0)
                .sorted()
                .toList();
    }
}