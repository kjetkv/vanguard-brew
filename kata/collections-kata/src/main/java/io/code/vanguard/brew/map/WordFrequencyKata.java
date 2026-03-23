package io.code.vanguard.brew.map;

import io.code.vanguard.brew.BasicKata;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class WordFrequencyKata implements BasicKata<List<String>, Map<String, Integer>> {

    @Override
    public Map<String, Integer> solve(List<String> input) {
        return input.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(s -> s, _ -> 1, Integer::sum));
    }
}