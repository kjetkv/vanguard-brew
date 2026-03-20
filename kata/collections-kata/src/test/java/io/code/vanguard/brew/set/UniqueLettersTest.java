package io.code.vanguard.brew.set;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Set;

@Tag("Set")
@DisplayName("Set - Unique Letters Only (String to Set)")
public class UniqueLettersTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a normal sentence with mixed casing, returns unique lowercase letters. " +
            "E.g.: 'Hello World' returns {'h', 'e', 'l', 'o', 'w', 'r', 'd'}")
    @Order(1)
    void testStandardSentence() {
        verifyBasicKata(new UniqueLettersKata(),
                "Hello World",
                Set.of('h', 'e', 'l', 'o', 'w', 'r', 'd'),
                Set::equals);
    }

    @Test
    @DisplayName("When receiving a chaotic string with numbers and symbols, filters out everything except letters. " +
            "E.g.: '123 Java!!! 456' returns {'j', 'a', 'v'}")
    @Order(2)
    void testChaoticString() {
        verifyBasicKata(new UniqueLettersKata(),
                "123 Java!!! 456 \t\n @Code",
                Set.of('j', 'a', 'v', 'c', 'o', 'd', 'e'),
                Set::equals);
    }

    @Test
    @DisplayName("When receiving a perfect pangram, successfully captures all 26 English letters. " +
            "E.g.: 'The quick brown fox jumps over the lazy dog'")
    @Order(3)
    void testPangram() {

        Set<Character> allLetters = new java.util.HashSet<>();
        for (char c = 'a'; c <= 'z'; c++) {
            allLetters.add(c);
        }

        verifyBasicKata(new UniqueLettersKata(),
                "The quick brown fox jumps over the lazy dog",
                allLetters,
                Set::equals);
    }

    @Test
    @DisplayName("When receiving a string containing absolutely no letters, returns an empty set. " +
            "E.g.: '12345 !?>< *&^%' returns {}")
    @Order(4)
    void testNoLettersAtAll() {
        verifyBasicKata(new UniqueLettersKata(),
                "12345 !?>< *&^%",
                Set.of(),
                Set::equals);
    }
}