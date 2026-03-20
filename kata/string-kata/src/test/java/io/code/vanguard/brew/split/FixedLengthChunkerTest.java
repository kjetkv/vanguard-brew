package io.code.vanguard.brew.split;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("String - Fixed-Length Chunker")
@Tag("String")
public class FixedLengthChunkerTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a clean multiple of the chunk size, splits it perfectly. " +
            "E.g.: 'abcdef' size 2 returns ['ab', 'cd', 'ef']")
    @Order(1)
    void testPerfectChunks() {
        verifyBasicKata(new FixedLengthChunkerKata(),
                new FixedLengthChunkerKata.ChunkRequest("Autobots", 4),
                List.of("Auto", "bots"),
                List::equals);
    }

    @Test
    @DisplayName("When the string length is not a perfect multiple, grabs the remainder for the last chunk. " +
            "E.g.: 'abcde' size 2 returns ['ab', 'cd', 'e']")
    @Order(2)
    void testUnevenChunks() {
        verifyBasicKata(new FixedLengthChunkerKata(),
                new FixedLengthChunkerKata.ChunkRequest("WakandaForever", 3),
                List.of("Wak", "and", "aFo", "rev", "er"),
                List::equals);
    }

    @Test
    @DisplayName("When the chunk size is 1, splits the string into individual characters. " +
            "E.g.: 'dog' size 1 returns ['d', 'o', 'g']")
    @Order(3)
    void testSingleCharacterChunks() {
        verifyBasicKata(new FixedLengthChunkerKata(),
                new FixedLengthChunkerKata.ChunkRequest("Groot", 1),
                List.of("G", "r", "o", "o", "t"),
                List::equals);
    }

    @Test
    @DisplayName("When the chunk size is larger than the string itself, returns the whole string as one chunk. " +
            "E.g.: 'cat' size 10 returns ['cat']")
    @Order(4)
    void testOversizedChunk() {
        verifyBasicKata(new FixedLengthChunkerKata(),
                new FixedLengthChunkerKata.ChunkRequest("HulkSmash", 50),
                List.of("HulkSmash"),
                List::equals);
    }

    @Test
    @DisplayName("When the string is completely empty, safely returns an empty list. " +
            "E.g.: '' size 5 returns []")
    @Order(5)
    void testEmptyString() {
        verifyBasicKata(new FixedLengthChunkerKata(),
                new FixedLengthChunkerKata.ChunkRequest("", 5),
                List.of(),
                List::equals);
    }
}