package io.code.vanguard.brew.streams;

import io.code.vanguard.brew.BasicKata;

import java.util.List;
import java.util.Map;

import static io.code.vanguard.brew.streams.BlendAggregatorKata.CoffeeBean;

public class BlendAggregatorKata implements BasicKata<List<CoffeeBean>, Map<Boolean, Map<String, Integer>>> {

    public record CoffeeBean(String origin, boolean isDecaf, int weightGrams) { }

    @Override
    public Map<Boolean, Map<String, Integer>> solve(List<CoffeeBean> batch) {
        return null;
    }
}