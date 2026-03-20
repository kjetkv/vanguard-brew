package io.code.vanguard.brew.regex;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

@DisplayName("String - Number Extractor")
@Tag("String")
public class NumberExtractorTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a string with multiple separate numbers, extracts and sums them. " +
            "E.g.: 'Buy 2 apples and 3 pears' returns 5")
    @Order(1)
    void testMultipleSeparateNumbers() {
        verifyBasicKata(new NumberExtractorKata(),
                "I have 3 dragons and 2 swords.",
                5,
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving multi-digit numbers, treats consecutive digits as a single number. " +
            "E.g.: 'Score 100 points and get 50 coins' returns 150")
    @Order(2)
    void testMultiDigitNumbers() {
        verifyBasicKata(new NumberExtractorKata(),
                "Level 99 Boss has 5000 HP!",
                5099,
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving a string with absolutely no numbers, returns 0. " +
            "E.g.: 'Just letters here' returns 0")
    @Order(3)
    void testNoNumbers() {
        verifyBasicKata(new NumberExtractorKata(),
                "There is no spoon.",
                0,
                Objects::equals);
    }

    @Test
    @DisplayName("When numbers are squished directly against letters, successfully isolates the digits. " +
            "E.g.: 'A1B2C3' returns 6")
    @Order(4)
    void testSquishedNumbers() {
        verifyBasicKata(new NumberExtractorKata(),
                "R2D2 meets C3PO",
                7,
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving a string made entirely of numbers, parses the whole string as one number. " +
            "E.g.: '123' returns 123")
    @Order(5)
    void testOnlyNumbers() {
        verifyBasicKata(new NumberExtractorKata(),
                "42",
                42,
                Objects::equals);
    }
}