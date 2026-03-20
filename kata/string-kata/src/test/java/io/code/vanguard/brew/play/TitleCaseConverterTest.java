package io.code.vanguard.brew.play;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

@DisplayName("String - Title Case Converter")
@Tag("String")
public class TitleCaseConverterTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a standard lowercase sentence, capitalizes the first letter of each word. " +
            "E.g.: 'java is fun' returns 'Java Is Fun'")
    @Order(1)
    void testStandardLowercase() {
        verifyBasicKata(new TitleCaseConverterKata(),
                "to infinity and beyond",
                "To Infinity And Beyond",
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving a string with chaotic mixed casing, normalizes the rest of the word to lowercase. " +
            "E.g.: 'cRaZy CaSe' returns 'Crazy Case'")
    @Order(2)
    void testMixedCasing() {
        verifyBasicKata(new TitleCaseConverterKata(),
                "wInTeR iS cOmInG",
                "Winter Is Coming",
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving single-letter words, safely capitalizes them without throwing substring errors. " +
            "E.g.: 'a cat' returns 'A Cat'")
    @Order(3)
    void testSingleLetters() {
        verifyBasicKata(new TitleCaseConverterKata(),
                "i am iron man",
                "I Am Iron Man",
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving strings with numbers and punctuation, handles them safely without changing them. " +
            "E.g.: 'player 1 start!' returns 'Player 1 Start!'")
    @Order(4)
    void testNumbersAndPunctuation() {
        verifyBasicKata(new TitleCaseConverterKata(),
                "g0tta catch 'em a11!",
                "G0tta Catch 'em A11!",
                Objects::equals);
    }
}