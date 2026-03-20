package io.code.vanguard.brew.manipulation;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

@DisplayName("String - Credit Card Masker")
@Tag("String")
public class CreditCardMaskerTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a standard 16-digit number, masks all but the last 4 digits. " +
            "E.g.: '1234567812345678' returns '############5678'")
    @Order(1)
    void testStandardSixteenDigits() {
        verifyBasicKata(new CreditCardMaskerKata(),
                "4532019988776543",
                "############6543",
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving a string containing dashes or spaces, treats them as normal characters to mask. " +
            "E.g.: '1234-5678' returns '####-5678'")
    @Order(2)
    void testStringWithSeparators() {
        verifyBasicKata(new CreditCardMaskerKata(),
                "867-5309-Jenny",
                "##########enny",
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving a string with exactly four characters, leaves it completely unmasked. " +
            "E.g.: 'abcd' returns 'abcd'")
    @Order(3)
    void testExactlyFourCharacters() {
        verifyBasicKata(new CreditCardMaskerKata(),
                "N7",
                "N7",
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving a string shorter than four characters, returns it completely unmasked. " +
            "E.g.: '12' returns '12'")
    @Order(4)
    void testShortString() {
        verifyBasicKata(new CreditCardMaskerKata(),
                "HAL",
                "HAL",
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving a very long text string, successfully masks everything up to the final four characters. " +
            "E.g.: 'SecretPassword123' returns '#############d123'")
    @Order(5)
    void testLongTextString() {
        verifyBasicKata(new CreditCardMaskerKata(),
                "ItsASecretToEverybody",
                "#################body",
                Objects::equals);
    }
}