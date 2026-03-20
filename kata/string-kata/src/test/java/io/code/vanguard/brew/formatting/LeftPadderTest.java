package io.code.vanguard.brew.formatting;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

@DisplayName("String - Left Padder")
@Tag("String")
public class LeftPadderTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the string is shorter than the target length, pads the left side with the specified character. " +
            "E.g.: '42' (length 5, char '0') returns '00042'")
    @Order(1)
    void testStandardPadding() {
        verifyBasicKata(new LeftPadderKata(),
                new LeftPadderKata.PadRequest("7", 3, '0'),
                "007",
                Objects::equals);
    }

    @Test
    @DisplayName("When padding with symbols or spaces, accurately repeats the character to reach the exact length. " +
            "E.g.: 'cat' (length 6, char '*') returns '***cat'")
    @Order(2)
    void testSymbolPadding() {
        verifyBasicKata(new LeftPadderKata(),
                new LeftPadderKata.PadRequest("Jedi", 10, '*'),
                "******Jedi",
                Objects::equals);
    }

    @Test
    @DisplayName("When the string is already the exact target length, returns the original string unpadded. " +
            "E.g.: 'hello' (length 5, char '-') returns 'hello'")
    @Order(3)
    void testExactLength() {
        verifyBasicKata(new LeftPadderKata(),
                new LeftPadderKata.PadRequest("Matrix", 6, ' '),
                "Matrix",
                Objects::equals);
    }

    @Test
    @DisplayName("When the target length is smaller than the string's current length, returns the original string safely. " +
            "E.g.: 'superman' (length 4, char '0') returns 'superman'")
    @Order(4)
    void testTargetLengthTooSmall() {
        verifyBasicKata(new LeftPadderKata(),
                new LeftPadderKata.PadRequest("Voldemort", 5, 'X'),
                "Voldemort",
                Objects::equals);
    }

    @Test
    @DisplayName("When the input string is completely empty, returns a string made entirely of the padding character. " +
            "E.g.: '' (length 4, char '-') returns '----'")
    @Order(5)
    void testEmptyString() {
        verifyBasicKata(new LeftPadderKata(),
                new LeftPadderKata.PadRequest("", 8, '-'),
                "--------",
                Objects::equals);
    }
}