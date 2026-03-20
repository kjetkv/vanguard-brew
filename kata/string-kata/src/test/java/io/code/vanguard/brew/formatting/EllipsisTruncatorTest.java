package io.code.vanguard.brew.formatting;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

@DisplayName("String - Ellipsis Truncator")
@Tag("String")
public class EllipsisTruncatorTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the string exceeds the max length, cuts the text and appends an ellipsis to match the exact max length. " +
            "E.g.: 'Hello World' (max 8) returns 'Hello...'")
    @Order(1)
    void testStandardTruncation() {
        verifyBasicKata(new EllipsisTruncatorKata(),
                new EllipsisTruncatorKata.TruncateRequest("With great power comes great responsibility", 16),
                "With great po...",
                Objects::equals);
    }

    @Test
    @DisplayName("When the string is exactly the maximum length, returns it completely unchanged. " +
            "E.g.: 'apple' (max 5) returns 'apple'")
    @Order(2)
    void testExactLength() {
        verifyBasicKata(new EllipsisTruncatorKata(),
                new EllipsisTruncatorKata.TruncateRequest("Avengers", 8),
                "Avengers",
                Objects::equals);
    }

    @Test
    @DisplayName("When the string is shorter than the maximum length, returns it completely unchanged. " +
            "E.g.: 'cat' (max 10) returns 'cat'")
    @Order(3)
    void testShorterThanMaxLength() {
        verifyBasicKata(new EllipsisTruncatorKata(),
                new EllipsisTruncatorKata.TruncateRequest("Thor", 15),
                "Thor",
                Objects::equals);
    }

    @Test
    @DisplayName("When the max length is exactly 3, safely returns just the ellipsis. " +
            "E.g.: 'long word' (max 3) returns '...'")
    @Order(4)
    void testMaxLengthOfThree() {
        verifyBasicKata(new EllipsisTruncatorKata(),
                new EllipsisTruncatorKata.TruncateRequest("Voldemort", 3),
                "...",
                Objects::equals);
    }

    @Test
    @DisplayName("When the max length is less than 3, safely returns a partial ellipsis without throwing a substring exception. " +
            "E.g.: 'huge' (max 2) returns '..'")
    @Order(5)
    void testExtremeEdgeCase() {
        verifyBasicKata(new EllipsisTruncatorKata(),
                new EllipsisTruncatorKata.TruncateRequest("Bazinga!", 2),
                "..",
                Objects::equals);
    }
}