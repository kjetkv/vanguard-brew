package io.code.vanguard.brew.list;

import io.code.vanguard.brew.BasicKata;

import java.util.List;

public class ReverseListKata implements BasicKata<List<Integer>, List<Integer>> {

    @Override
    public List<Integer> solve(List<Integer> input) {
        return input.reversed();
    }
}
