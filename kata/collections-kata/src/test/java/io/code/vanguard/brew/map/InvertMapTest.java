package io.code.vanguard.brew.map;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Tag("Map")
@DisplayName("Map - Invert the Map")
public class InvertMapTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a map with unique values, correctly swaps all keys and values. " +
            "E.g.: {'A'='1', 'B'='2'} returns {'1'='A', '2'='B'}")
    @Order(1)
    void testStandardInversion() {
        verifyBasicKata(new InvertMapKata(),
                Map.of("A", "1", "B", "2", "C", "3"),
                Map.of("1", "A", "2", "B", "3", "C"),
                Map::equals);
    }

    @Test
    @DisplayName("When receiving an empty map, returns a new empty map. " +
            "E.g.: {} returns {}")
    @Order(2)
    void testEmptyMap() {
        verifyBasicKata(new InvertMapKata(),
                Map.of(),
                Map.of(),
                Map::equals);
    }

/*    @Test
    @DisplayName("When receiving a null input, safely returns an empty map. " +
            "E.g.: null returns {}")
    @Order(3)
    void testNullInput() {
        verify(new InvertMapKata(),
                null,
                Map.of(),
                Map::equals);
    }*/

    @Test
    @DisplayName("When the original map has duplicate values, the new map keeps the last processed key. " +
            "E.g.: {'First'='X', 'Second'='X'} returns {'X'='Second'}")
    @Order(4)
    void testDuplicateValuesCollision() {
        // We use a LinkedHashMap to guarantee 'First' is processed before 'Second'
        Map<String, String> inputMap = new LinkedHashMap<>();
        inputMap.put("First", "X");
        inputMap.put("Second", "X");

        verifyBasicKata(new InvertMapKata(),
                inputMap,
                Map.of("X", "Second"),
                Map::equals);
    }

    @Test
    @DisplayName("When the original map has a null value, it successfully uses null as a key in the new map. " +
            "E.g.: {'Unknown'=null} returns {null='Unknown'}")
    @Order(5)
    void testNullValueBecomesNullKey() {

        // HashMaps perfectly support having exactly one null key! This is a great fun fact...
        Map<String, String> inputMap = new HashMap<>();
        inputMap.put("Unknown", null);

        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put(null, "Unknown");

        verifyBasicKata(new InvertMapKata(),
                inputMap,
                expectedMap,
                Map::equals);
    }
}