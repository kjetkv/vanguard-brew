package io.code.vanguard.brew.play;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

@DisplayName("String - Word Reverser")
@Tag("String")
public class WordReverserTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a standard sentence, reverses the order of the words. " +
            "E.g.: 'the quick brown fox' returns 'fox brown quick the'")
    @Order(1)
    void testStandardSentence() {
        verifyBasicKata(new WordReverserKata(),
                "May the Force be with you",
                "you with be Force the May",
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving a sentence with punctuation attached to words, moves the punctuation with the word. " +
            "E.g.: 'hello world!' returns 'world! hello'")
    @Order(2)
    void testSentenceWithPunctuation() {
        verifyBasicKata(new WordReverserKata(),
                "Roads? Where we're going, we don't need roads.",
                "roads. need don't we going, we're Where Roads?",
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving a single word, returns the exact same word. " +
            "E.g.: 'java' returns 'java'")
    @Order(3)
    void testSingleWord() {
        verifyBasicKata(new WordReverserKata(),
                "Supercalifragilisticexpialidocious",
                "Supercalifragilisticexpialidocious",
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving a sentence with numbers and symbols, reverses the words safely. " +
            "E.g.: 'ready player 1' returns '1 player ready'")
    @Order(4)
    void testNumbersAndSymbols() {
        verifyBasicKata(new WordReverserKata(),
                "It's over 9000 !!!",
                "!!! 9000 over It's",
                Objects::equals);
    }
}