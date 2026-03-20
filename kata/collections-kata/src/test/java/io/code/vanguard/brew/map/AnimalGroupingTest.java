package io.code.vanguard.brew.map;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

@Tag("Map")
@DisplayName("Map - Group Animals by Species")
public class AnimalGroupingTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a mixed list, correctly groups them by species. " +
            "E.g.: [Dog, Cat, Dog] returns {'Dog'=[Dog, Dog], 'Cat'=[Cat]}")
    @Order(1)
    void testStandardGrouping() {
        var dog1 = new AnimalGroupingKata.Animal("1", "Dog", "Rover");
        var dog2 = new AnimalGroupingKata.Animal("2", "Dog", "Spot");
        var cat1 = new AnimalGroupingKata.Animal("3", "Cat", "Whiskers");

        verifyBasicKata(new AnimalGroupingKata(),
                List.of(dog1, cat1, dog2),
                Map.of("Dog", List.of(dog1, dog2),
                        "Cat", List.of(cat1)),
                Map::equals);
    }

    @Test
    @DisplayName("When all animals are the exact same species, returns a map with a single key. " +
            "E.g.: [Bird, Bird] returns {'Bird'=[Bird, Bird]}")
    @Order(2)
    void testSingleSpeciesGrouping() {
        var bird1 = new AnimalGroupingKata.Animal("B1", "Bird", "Tweety");
        var bird2 = new AnimalGroupingKata.Animal("B2", "Bird", "Polly");

        verifyBasicKata(new AnimalGroupingKata(),
                List.of(bird1, bird2),
                Map.of("Bird", List.of(bird1, bird2)),
                Map::equals);
    }

    @Test
    @DisplayName("When every animal is a unique species, returns a map where every list has size 1. " +
            "E.g.: [Dog, Cat, Fish] returns {'Dog'=[Dog], 'Cat'=[Cat], 'Fish'=[Fish]}")
    @Order(3)
    void testAllUniqueSpecies() {
        var dog = new AnimalGroupingKata.Animal("D", "Dog", "Rex");
        var cat = new AnimalGroupingKata.Animal("C", "Cat", "Luna");
        var fish = new AnimalGroupingKata.Animal("F", "Fish", "Nemo");

        verifyBasicKata(new AnimalGroupingKata(),
                List.of(dog, cat, fish),
                Map.of("Dog", List.of(dog),
                        "Cat", List.of(cat),
                        "Fish", List.of(fish)),
                Map::equals);
    }

    @Test
    @DisplayName("When an animal has a null species, correctly groups them under the 'Unknown' key. " +
            "E.g.: [species=null] returns {'Unknown'=[Animal]}")
    @Order(4)
    void testNullSpeciesGrouping() {
        // This reinforces that HashMaps can handle a null key, which acts as a default "Unknown" bucket!
        var unknownAnimal = new AnimalGroupingKata.Animal("U", null, "Mystery");

        // Using HashMap explicitly because Map.of() rejects null keys
        Map<String, List<AnimalGroupingKata.Animal>> expectedMap = new java.util.HashMap<>();
        expectedMap.put("Unknown", List.of(unknownAnimal));

        verifyBasicKata(new AnimalGroupingKata(),
                List.of(unknownAnimal),
                expectedMap,
                Map::equals);
    }
}