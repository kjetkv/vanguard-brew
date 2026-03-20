package io.code.vanguard.brew.list;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@Tag("List")
@DisplayName("List - The Number Doubler")
public class NumberDoublerTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a list of positive integers, returns a list with all values doubled. " +
            "E.g.: [1, 2, 3] returns [2, 4, 6]")
    @Order(1)
    void testDoublePositiveNumbers() {
        verifyBasicKata(new NumberDoublerKata(),
                List.of(1, 2, 3),
                List.of(2, 4, 6),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a list of negative integers, returns a list with all values doubled (more negative). " +
            "E.g.: [-1, -5] returns [-2, -10]")
    @Order(2)
    void testDoubleNegativeNumbers() {
        verifyBasicKata(new NumberDoublerKata(),
                List.of(-1, -5),
                List.of(-2, -10),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a list of zeros, returns a list of zeros. " +
            "E.g.: [0, 0] returns [0, 0]")
    @Order(3)
    void testDoubleZeros() {
        verifyBasicKata(new NumberDoublerKata(),
                List.of(0, 0),
                List.of(0, 0),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a mixed list of positives, negatives, and zeros, doubles them all correctly. " +
            "E.g.: [10, -3, 0] returns [20, -6, 0]")
    @Order(4)
    void testMixedNumbers() {
        verifyBasicKata(new NumberDoublerKata(),
                List.of(10, -3, 0, 5),
                List.of(20, -6, 0, 10),
                List::equals);
    }

    @Test
    @DisplayName("When receiving an empty list, returns a new empty list. " +
            "E.g.: [] returns []")
    @Order(5)
    void testEmptyList() {
        verifyBasicKata(new NumberDoublerKata(),
                List.of(),
                List.of(),
                List::equals);
    }
}
