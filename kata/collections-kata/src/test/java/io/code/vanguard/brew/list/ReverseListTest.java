package io.code.vanguard.brew.list;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@Tag("List")
@DisplayName("List - Reverse the List")
public class ReverseListTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a standard list of numbers, returns them in reverse order. " +
            "E.g.: [1, 2, 3] returns [3, 2, 1]")
    @Order(1)
    void testStandardReverse() {
        verifyBasicKata(new ReverseListKata(),
                List.of(1, 2, 3, 4),
                List.of(4, 3, 2, 1),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a list with negative numbers, correctly reverses their order. " +
            "E.g.: [-1, -2, -3] returns [-3, -2, -1]")
    @Order(2)
    void testNegativeNumbersReverse() {
        verifyBasicKata(new ReverseListKata(),
                List.of(-1, -2, -3),
                List.of(-3, -2, -1),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a palindromic list (reads the same forward and backward), returns the exact same list. " +
            "E.g.: [1, 2, 1] returns [1, 2, 1]")
    @Order(3)
    void testPalindromeList() {
        verifyBasicKata(new ReverseListKata(),
                List.of(1, 2, 3, 2, 1),
                List.of(1, 2, 3, 2, 1),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a single-element list, returns the same single element. " +
            "E.g.: [42] returns [42]")
    @Order(4)
    void testSingleElementList() {
        verifyBasicKata(new ReverseListKata(),
                List.of(42),
                List.of(42),
                List::equals);
    }
}
