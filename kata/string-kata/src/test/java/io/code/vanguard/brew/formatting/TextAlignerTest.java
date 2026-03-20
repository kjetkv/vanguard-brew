package io.code.vanguard.brew.formatting;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("String - Text Aligner")
@Tag("String")
public class TextAlignerTest extends BasicKataTestBase {

    @Test
    @DisplayName("When providing a list of varying lengths, right-aligns them to match the longest word. " +
            "E.g.: ['cat', 'hippopotamus'] returns ['         cat', 'hippopotamus']")
    @Order(1)
    void testStandardAlignment() {
        verifyBasicKata(new TextAlignerKata(),
                List.of("Han", "Luke", "Chewbacca"),
                List.of("      Han", "     Luke", "Chewbacca"),
                List::equals);
    }

    @Test
    @DisplayName("When all strings are already the exact same length, returns the list completely unchanged. " +
            "E.g.: ['one', 'two', 'six'] returns ['one', 'two', 'six']")
    @Order(2)
    void testAlreadyAligned() {
        verifyBasicKata(new TextAlignerKata(),
                List.of("Neo", "Fly", "Doc"),
                List.of("Neo", "Fly", "Doc"),
                List::equals);
    }

    @Test
    @DisplayName("When providing a list with a single item, returns it unchanged since it is the maximum length. " +
            "E.g.: ['apple'] returns ['apple']")
    @Order(3)
    void testSingleItem() {
        verifyBasicKata(new TextAlignerKata(),
                List.of("Batman"),
                List.of("Batman"),
                List::equals);
    }

    @Test
    @DisplayName("When the list contains completely empty strings, pads them with spaces up to the maximum length. " +
            "E.g.: ['a', '', 'abc'] returns ['  a', '   ', 'abc']")
    @Order(4)
    void testListWithEmptyStrings() {
        verifyBasicKata(new TextAlignerKata(),
                List.of("Stark", "", "Thor"),
                List.of("Stark", "     ", " Thor"),
                List::equals);
    }

    @Test
    @DisplayName("When the list is completely empty, safely returns an empty list. " +
            "E.g.: [] returns []")
    @Order(5)
    void testEmptyList() {
        verifyBasicKata(new TextAlignerKata(),
                List.of(),
                List.of(),
                List::equals);
    }
}