package io.code.vanguard.brew.map;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

@Tag("Map")
@DisplayName("Map - Word Frequency Counter")
public class WordFrequencyTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a list with some duplicates, returns a map with accurate counts. " +
            "E.g.: ['apple', 'banana', 'apple'] returns {'apple'=2, 'banana'=1}")
    @Order(1)
    void testStandardFrequencies() {
        verifyBasicKata(new WordFrequencyKata(),
                List.of("apple", "banana", "apple"),
                Map.of("apple", 2, "banana", 1),
                Map::equals);
    }

    @Test
    @DisplayName("When receiving a list of entirely unique words, every word gets a count of 1. " +
            "E.g.: ['cat', 'dog', 'bird'] returns {'cat'=1, 'dog'=1, 'bird'=1}")
    @Order(2)
    void testAllUniqueWords() {
        verifyBasicKata(new WordFrequencyKata(),
                List.of("cat", "dog", "bird"),
                Map.of("cat", 1, "dog", 1, "bird", 1),
                Map::equals);
    }

    @Test
    @DisplayName("When receiving a list where all words are identical, returns one entry with the total size. " +
            "E.g.: ['java', 'java', 'java'] returns {'java'=3}")
    @Order(3)
    void testAllIdenticalWords() {
        verifyBasicKata(new WordFrequencyKata(),
                List.of("java", "java", "java", "java"),
                Map.of("java", 4),
                Map::equals);
    }

    @Test
    @DisplayName("When receiving differently cased words, it treats them as completely separate entries. " +
            "E.g.: ['Apple', 'apple'] returns {'Apple'=1, 'apple'=1}")
    @Order(4)
    void testCaseSensitivity() {
        // This is a great teaching moment: Maps compare keys strictly using .equals() and .hashCode()
        verifyBasicKata(new WordFrequencyKata(),
                List.of("Apple", "apple", "APPLE", "apple"),
                Map.of("Apple", 1, "apple", 2, "APPLE", 1),
                Map::equals);
    }

    @Test
    @DisplayName("When receiving a list containing null elements, it safely ignores them and counts the valid words. " +
            "E.g.: ['apple', null, 'apple'] returns {'apple'=2}")
    @Order(5)
    void testNullsInList() {
        // We must use Arrays.asList() here because List.of() strictly forbids nulls
        verifyBasicKata(new WordFrequencyKata(),
                java.util.Arrays.asList("apple", null, "apple", null),
                Map.of("apple", 2),
                Map::equals);
    }
}