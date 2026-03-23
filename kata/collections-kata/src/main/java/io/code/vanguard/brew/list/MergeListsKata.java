package io.code.vanguard.brew.list;

import io.code.vanguard.brew.BasicKata;

import java.util.Collection;
import java.util.List;

public class MergeListsKata implements BasicKata<List<List<Integer>>, List<Integer>> {

    @Override
    public List<Integer> solve(List<List<Integer>> input) {
        return input.stream()
                .flatMap(Collection::stream)
                .toList();
    }
}