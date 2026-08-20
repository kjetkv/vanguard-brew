package io.code.vanguard.brew.streams;

import io.code.vanguard.brew.BasicKata;

import java.util.List;

import static io.code.vanguard.brew.streams.HopperPipelineKata.CoffeeBean;
import static io.code.vanguard.brew.streams.HopperPipelineKata.DeliveryTruck;

public class HopperPipelineKata implements BasicKata<List<DeliveryTruck>, List<CoffeeBean>> {

    public record CoffeeBean(String origin) { }

    public record Sack(List<CoffeeBean> beans) { }

    public record Pallet(List<Sack> sacks) { }

    public record DeliveryTruck(List<Pallet> pallets) { }

    @Override
    public List<CoffeeBean> solve(List<DeliveryTruck> trucks) {
        return null;
    }
}