package io.code.vanguard.brew.list;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@Tag("List")
@DisplayName("List - String Lengths")
public class StringLengthsTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a list of standard words, returns a list of their character counts. " +
            "E.g.: ['apple', 'cat'] returns [5, 3]")
    @Order(1)
    void testStandardWords() {
        verifyBasicKata(new StringLengthsKata(),
                List.of("apple", "cat", "banana"),
                List.of(5, 3, 6),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a list containing empty strings, correctly counts them as 0 length. " +
            "E.g.: ['a', '', 'b'] returns [1, 0, 1]")
    @Order(2)
    void testEmptyStrings() {
        verifyBasicKata(new StringLengthsKata(),
                List.of("a", "", "b"),
                List.of(1, 0, 1),
                List::equals);
    }

    @Test
    @DisplayName("When receiving strings with spaces, it counts every character including the spaces. " +
            "E.g.: ['hello world', ' '] returns [11, 1]")
    @Order(3)
    void testStringsWithSpaces() {
        verifyBasicKata(new StringLengthsKata(),
                List.of("hello world", " ", "java 25"),
                List.of(11, 1, 7),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a single very long string, it correctly calculates the length. " +
            "E.g.: ['supercalifragilisticexpialidocious'] returns [34]")
    @Order(4)
    void testSingleLongString() {
        verifyBasicKata(new StringLengthsKata(),
                List.of("supercalifragilisticexpialidocious"),
                List.of(34),
                List::equals);
    }

    @Test
    @DisplayName("When receiving an empty list, returns a new empty list of integers. " +
            "E.g.: [] returns []")
    @Order(5)
    void testEmptyList() {
        verifyBasicKata(new StringLengthsKata(),
                List.of(),
                List.of(),
                List::equals);
    }
}
