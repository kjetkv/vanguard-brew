package io.code.vanguard.brew.play;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

@DisplayName("String - Vowel Counter")
@Tag("String")
public class VowelCounterTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a standard sentence, counts all the vowels accurately. " +
            "E.g.: 'The quick brown fox' returns 5")
    @Order(1)
    void testStandardSentence() {
        verifyBasicKata(new VowelCounterKata(),
                "May the Force be with you.",
                8,
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving a string with mixed casing and wild punctuation, only counts the vowels. " +
            "E.g.: 'SpOnGeBoB cAsE!' returns 4")
    @Order(2)
    void testMixedCaseAndPunctuation() {
        verifyBasicKata(new VowelCounterKata(),
                "Wubba Lubba Dub-Dub!!! 🚀",
                6,
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving a string with absolutely no vowels, returns 0. " +
            "E.g.: 'rhythm fly slyly' returns 0")
    @Order(3)
    void testNoVowels() {
        verifyBasicKata(new VowelCounterKata(),
                "Shh! Crypt lynx cwm tryst... 🤫",
                0,
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving a string heavily loaded with vowels and numbers, counts every single vowel. " +
            "E.g.: '123 aeiou 456' returns 5")
    @Order(4)
    void testHeavyVowelsAndNumbers() {
        verifyBasicKata(new VowelCounterKata(),
                "Up, up, down, down, left, right, left, right, B, A, Start!",
                10,
                Objects::equals);
    }

/*    @Test
    @DisplayName("When receiving an empty string or null, safely returns 0. " +
            "E.g.: '' or null returns 0")
    @Order(5)
    void testEmptyAndNull() {
        verify(new VowelCounterKata(),
                "",
                0,
                Objects::equals);

        verify(new VowelCounterKata(),
                null,
                0,
                Objects::equals);
    }*/
}
