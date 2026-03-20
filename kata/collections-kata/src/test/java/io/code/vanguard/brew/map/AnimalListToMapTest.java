package io.code.vanguard.brew.map;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

@Tag("Map")
@DisplayName("Map - List to Map (Animal Indexer)")
public class AnimalListToMapTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a list of unique animals, indexes them by their ID. " +
            "E.g.: [{id='A1', name='Leo'}] returns {'A1'={id='A1', name='Leo'}}")
    @Order(1)
    void testStandardIndexing() {
        var leo = new AnimalListToMapKata.Animal("A1", "Lion", "Leo");
        var polly = new AnimalListToMapKata.Animal("B2", "Parrot", "Polly");

        verifyBasicKata(new AnimalListToMapKata(),
                List.of(leo, polly),
                Map.of("A1", leo, "B2", polly),
                Map::equals);
    }

    @Test
    @DisplayName("When the list contains animals with the same ID, the last one overwrites the previous ones. " +
            "E.g.: [{id='X1', name='Dog'}, {id='X1', name='Cat'}] returns {'X1'={Cat}}")
    @Order(2)
    void testDuplicateIdsOverwriting() {
        var dog = new AnimalListToMapKata.Animal("X1", "Dog", "Rover");
        var cat = new AnimalListToMapKata.Animal("X1", "Cat", "Whiskers");

        verifyBasicKata(new AnimalListToMapKata(),
                List.of(dog, cat),
                Map.of("X1", cat),
                Map::equals);
    }

    @Test
    @DisplayName("When receiving a list containing a null element, safely skips it without crashing. " +
            "E.g.: [Animal, null, Animal] returns a map with 2 entries.")
    @Order(3)
    void testListWithNullElement() {
        var turtle = new AnimalListToMapKata.Animal("T9", "Turtle", "Shelly");

        verifyBasicKata(new AnimalListToMapKata(),
                java.util.Arrays.asList(turtle, null),
                Map.of("T9", turtle),
                Map::equals);
    }

    @Test
    @DisplayName("When receiving an animal with a null ID, safely skips it so we don't create a null key. " +
            "E.g.: [{id=null, name='Ghost'}] returns {}")
    @Order(4)
    void testAnimalWithNullId() {
        var ghostAnimal = new AnimalListToMapKata.Animal(null, "Unknown", "Ghost");

        verifyBasicKata(new AnimalListToMapKata(),
                List.of(ghostAnimal),
                Map.of(),
                Map::equals);
    }

    @Test
    @DisplayName("When receiving an empty list, returns an empty map. " +
            "E.g.: [] returns {}")
    @Order(5)
    void testEmptyList() {
        verifyBasicKata(new AnimalListToMapKata(),
                List.of(),
                Map.of(),
                Map::equals);
    }
}