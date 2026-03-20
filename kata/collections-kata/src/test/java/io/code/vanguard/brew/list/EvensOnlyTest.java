package io.code.vanguard.brew.list;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@Tag("List")
@DisplayName("List - Evens Only")
public class EvensOnlyTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a mixed list of odd and even numbers, returns only the evens. " +
            "E.g.: [1, 2, 3, 4] returns [2, 4]")
    @Order(1)
    void testMixedNumbers() {
        verifyBasicKata(new EvensOnlyKata(),
                List.of(1, 2, 3, 4, 5, 8),
                List.of(2, 4, 8),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a list of entirely odd numbers, returns an empty list. " +
            "E.g.: [1, 3, 5] returns []")
    @Order(2)
    void testAllOdds() {
        verifyBasicKata(new EvensOnlyKata(),
                List.of(1, 3, 5, 7, 9),
                List.of(),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a list of entirely even numbers, returns all of them. " +
            "E.g.: [2, 10, 4] returns [2, 10, 4]")
    @Order(3)
    void testAllEvens() {
        verifyBasicKata(new EvensOnlyKata(),
                List.of(2, 10, 4, 6),
                List.of(2, 10, 4, 6),
                List::equals);
    }

    @Test
    @DisplayName("When the list contains zero, correctly identifies 0 as an even number. " +
            "E.g.: [0, 7, 4] returns [0, 4]")
    @Order(4)
    void testIncludesZero() {
        verifyBasicKata(new EvensOnlyKata(),
                List.of(0, 7, 4, 1),
                List.of(0, 4),
                List::equals);
    }

    @Test
    @DisplayName("When the list contains negative numbers, correctly filters the negative evens. " +
            "E.g.: [-2, -3, -4] returns [-2, -4]")
    @Order(5)
    void testNegativeNumbers() {
        // This is a great test because beginners sometimes write `num % 2 == 1` for odds instead of `!= 0`,
        // which breaks on negatives! Testing evens with `% 2 == 0` is safer, and this proves it works.
        verifyBasicKata(new EvensOnlyKata(),
                List.of(-2, -3, -4, 5, -8),
                List.of(-2, -4, -8),
                List::equals);
    }
}
