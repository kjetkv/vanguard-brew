package io.code.vanguard.brew.map;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

@Tag("Map")
@DisplayName("Map - Adults Only Filter")
public class AdultsOnlyFilterTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a mixed map of ages, returns only those 18 and older. " +
            "E.g.: {'Alice'=25, 'Bob'=15, 'Charlie'=30} returns {'Alice'=25, 'Charlie'=30}")
    @Order(1)
    void testMixedAges() {
        verifyBasicKata(new AdultsOnlyFilterKata(),
                Map.of("Alice", 25, "Bob", 15, "Charlie", 30),
                Map.of("Alice", 25, "Charlie", 30),
                Map::equals);
    }

    @Test
    @DisplayName("When receiving a map where everyone is an adult, returns the exact same map. " +
            "E.g.: {'Dave'=40, 'Eve'=55} returns {'Dave'=40, 'Eve'=55}")
    @Order(2)
    void testAllAdults() {
        verifyBasicKata(new AdultsOnlyFilterKata(),
                Map.of("Dave", 40, "Eve", 55),
                Map.of("Dave", 40, "Eve", 55),
                Map::equals);
    }

    @Test
    @DisplayName("When receiving a map where everyone is a minor, returns an empty map. " +
            "E.g.: {'Timmy'=5, 'Sally'=12} returns {}")
    @Order(3)
    void testAllMinors() {
        verifyBasicKata(new AdultsOnlyFilterKata(),
                Map.of("Timmy", 5, "Sally", 12),
                Map.of(),
                Map::equals);
    }

    @Test
    @DisplayName("When checking the exact boundary age of 18, correctly includes them. " +
            "E.g.: {'Eighteen'=18, 'Seventeen'=17} returns {'Eighteen'=18}")
    @Order(4)
    void testBoundaryAge() {
        // Beginners often mistakenly use > 18 instead of >= 18. This test catches that bug!
        verifyBasicKata(new AdultsOnlyFilterKata(),
                Map.of("Eighteen", 18, "Seventeen", 17),
                Map.of("Eighteen", 18),
                Map::equals);
    }

    @Test
    @DisplayName("When receiving a map with a null value, safely ignores it without throwing an error. " +
            "E.g.: {'Unknown'=null, 'John'=20} returns {'John'=20}")
    @Order(5)
    void testNullAgeValues() {
        // Using an inline map builder setup since Map.of() doesn't allow null values
        Map<String, Integer> inputMap = new java.util.HashMap<>();
        inputMap.put("Unknown", null);
        inputMap.put("John", 20);

        verifyBasicKata(new AdultsOnlyFilterKata(),
                inputMap,
                Map.of("John", 20),
                Map::equals);
    }
}
