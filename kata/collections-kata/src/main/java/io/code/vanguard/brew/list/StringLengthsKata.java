package io.code.vanguard.brew.list;

import io.code.vanguard.brew.BasicKata;

import java.util.List;

public class StringLengthsKata implements BasicKata<List<String>, List<Integer>> {

    @Override
    public List<Integer> solve(List<String> input) {
        return input.stream()
                .map(String::length)
                .toList();
    }
}
