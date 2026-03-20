package io.code.vanguard.brew.list;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@Tag("List")
@DisplayName("List - Positive Integer Sort")
public class PositiveIntegerSortTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving an unsorted list of positive integers, returns them sorted in ascending order. " +
            "E.g.: [5, 2, 8] returns [2, 5, 8]")
    @Order(1)
    void testStandardPositiveIntegers() {
        verifyBasicKata(new PositiveIntegerSortKata(),
                List.of(5, 2, 8, 1, 9),
                List.of(1, 2, 5, 8, 9),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a list containing zeros, the zeros are removed and the rest are sorted. " +
            "E.g.: [0, 3, 0, 1] returns [1, 3]")
    @Order(2)
    void testListWithZeros() {
        verifyBasicKata(new PositiveIntegerSortKata(),
                List.of(0, 5, 0, 1, 3, 0),
                List.of(1, 3, 5),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a list containing negative numbers, the negatives are removed and the rest are sorted. " +
            "E.g.: [-5, 4, -1, 2] returns [2, 4]")
    @Order(3)
    void testListWithNegativeNumbers() {
        verifyBasicKata(new PositiveIntegerSortKata(),
                List.of(-10, 7, -3, 2, -1, 5),
                List.of(2, 5, 7),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a list where no numbers are strictly above zero, returns an empty list. " +
            "E.g.: [-2, 0, -5] returns []")
    @Order(4)
    void testListWithNoValidNumbers() {
        verifyBasicKata(new PositiveIntegerSortKata(),
                List.of(-5, 0, -1, -99, 0),
                List.of(),
                List::equals);
    }
}