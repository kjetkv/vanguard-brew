package io.code.vanguard.brew.map;

import io.code.vanguard.brew.BasicKata;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class AdultsOnlyFilterKata implements BasicKata<Map<String, Integer>, Map<String, Integer>> {

    @Override
    public Map<String, Integer> solve(Map<String, Integer> input) {
        return input.entrySet().stream()
                .filter(Objects::nonNull)
                .filter(entry -> entry.getValue() != null && entry.getValue() >= 18)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
