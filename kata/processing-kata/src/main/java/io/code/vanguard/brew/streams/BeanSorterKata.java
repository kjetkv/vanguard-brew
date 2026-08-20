package io.code.vanguard.brew.streams;

import io.code.vanguard.brew.BasicKata;

import java.util.List;

import static io.code.vanguard.brew.streams.BeanSorterKata.Cherry;
import static io.code.vanguard.brew.streams.BeanSorterKata.CoffeeBean;

public class BeanSorterKata implements BasicKata<List<Cherry>, List<CoffeeBean>> {

    public record CoffeeBean(String origin, int densityScore) { }

    public record Cherry(boolean isDefective, CoffeeBean innerBean) { }

    @Override
    public List<CoffeeBean> solve(List<Cherry> batch) {
        return null;
    }
}