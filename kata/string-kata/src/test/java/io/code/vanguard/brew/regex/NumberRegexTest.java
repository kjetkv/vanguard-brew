package io.code.vanguard.brew.regex;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.code.vanguard.brew.Extractors.regexGroupsAsString;

@DisplayName("String - Number Finder")
@Tag("String")
public class NumberRegexTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the text contains isolated numbers, captures each number as a single match. " +
            "E.g.: 'Buy 2 get 1 free' returns a regex that finds '2,1'")
    @Order(1)
    void testIsolatedNumbers() {
        verifyNoArgKata(new NumberRegexKata(),
                "1,42",
                regex -> regexGroupsAsString.apply(regex, "Ready Player 1, collect 42 coins."),
                String::equals);
    }

    @Test
    @DisplayName("When the text contains consecutive digits, captures them together as a single number. " +
            "E.g.: 'Score: 9000' returns a regex that finds '9000'")
    @Order(2)
    void testConsecutiveDigits() {

        verifyNoArgKata(new NumberRegexKata(),
                "9001",
                regex -> regexGroupsAsString.apply(regex, "His power level is over 9001!"),
                String::equals);
    }

    @Test
    @DisplayName("When numbers are glued directly to letters, isolates and extracts just the digits. " +
            "E.g.: 'A1B2' returns a regex that finds '1,2'")
    @Order(3)
    void testNumbersGluedToLetters() {
        verifyNoArgKata(new NumberRegexKata(),
                "2,2,3,0",
                regex -> regexGroupsAsString.apply(regex, "R2-D2 and C-3P0"),
                String::equals);
    }

    @Test
    @DisplayName("When the text contains absolutely no numbers, returns a regex that finds an empty list. " +
            "E.g.: 'Just words' returns a regex that finds ''")
    @Order(4)
    void testNoNumbers() {
        verifyNoArgKata(new NumberRegexKata(),
                "",
                regex -> regexGroupsAsString.apply(regex, "To infinity and beyond"),
                String::equals);
    }

    @Test
    @DisplayName("When the text is composed entirely of a single long number, captures the whole string. " +
            "E.g.: '12345' returns a regex that finds '12345'")
    @Order(5)
    void testOnlyNumbers() {
        verifyNoArgKata(new NumberRegexKata(),
                "8675309",
                regex -> regexGroupsAsString.apply(regex, "8675309"),
                String::equals);
    }
}
