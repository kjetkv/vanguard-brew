package io.code.vanguard.brew.split;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

@DisplayName("String - Separator Splitter")
@Tag("String")
public class SeparatorSplitTest extends BasicKataTestBase {

    @Test
    @DisplayName(
            "When receiving a string containing ' ' as a separator, returns an array containing all of the parts of the string " +
                    "E.g.: 'one two' returns ['one', 'two']")
    @Order(1)
    void testSplitStringWithSpace() {
        verifyBasicKata(new StringSplitKata(),
                "public static void",
                new String[]{"public", "static", "void"},
                Arrays::equals);
    }

    @Test
    @DisplayName(
            "When receiving a string containing ';' as a separator, returns an array containing all of the parts of the string " +
                    "E.g.: 'one;two' returns ['one', 'two']")
    @Order(2)
    void testSplitStringWithSemicolon() {
        verifyBasicKata(new StringSplitKata(),
                "public;static;void",
                new String[]{"public", "static", "void"},
                Arrays::equals);
    }

    @Test
    @DisplayName(
            "When receiving a string containing ';' or ' ' as a separator, returns an array containing all of the parts of the string " +
                    "E.g.: 'one two;full' returns ['one', 'two', 'full']")
    @Order(3)
    void testSplitStringWithSemicolonOrBlank() {
        verifyBasicKata(new StringSplitKata(),
                "public static;void main;interface boat @ car",
                new String[]{"public", "static", "void", "main", "interface", "boat", "@", "car"},
                Arrays::equals);
    }
}
