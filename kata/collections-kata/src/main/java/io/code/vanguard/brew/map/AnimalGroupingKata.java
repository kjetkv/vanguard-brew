package io.code.vanguard.brew.map;

import io.code.vanguard.brew.BasicKata;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class AnimalGroupingKata implements BasicKata<List<AnimalGroupingKata.Animal>, Map<String, List<AnimalGroupingKata.Animal>>> {

    public static final String UNKNOWN_SPECIES = "Unknown";

    public record Animal(String id, String species, String name) {
    }

    @Override
    public Map<String, List<Animal>> solve(List<Animal> input) {
        return input.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(animal -> Objects.requireNonNullElse(animal.species, UNKNOWN_SPECIES)));
    }
}