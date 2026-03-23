package io.code.vanguard.brew.map;

import io.code.vanguard.brew.BasicKata;

import java.util.Map;
import java.util.stream.Collectors;

public class InvertMapKata implements BasicKata<Map<String, String>, Map<String, String>> {

    @Override
    public Map<String, String> solve(Map<String, String> input) {
        return input.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey, (_, right) -> right));
    }
}
