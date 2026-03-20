package io.code.vanguard.brew.list;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

@Tag("List")
@DisplayName("List - Find the Maximum Value")
public class FindMaximumValueTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the maximum value is in the middle of the list. " +
            "E.g.: [1, 9, 3] returns 9")
    @Order(1)
    void testMaxInMiddle() {
        verifyBasicKata(new FindMaximumValueKata(),
                List.of(1, 9, 3, 5),
                9,
                Objects::equals);
    }

    @Test
    @DisplayName("When the maximum value is at the very beginning of the list. " +
            "E.g.: [10, 2, 4] returns 10")
    @Order(2)
    void testMaxAtStart() {
        verifyBasicKata(new FindMaximumValueKata(),
                List.of(10, 2, 4, 1),
                10,
                Objects::equals);
    }

    @Test
    @DisplayName("When the maximum value is at the very end of the list. " +
            "E.g.: [2, 4, 8] returns 8")
    @Order(3)
    void testMaxAtEnd() {
        verifyBasicKata(new FindMaximumValueKata(),
                List.of(2, 4, 1, 8),
                8,
                Objects::equals);
    }

    @Test
    @DisplayName("When all numbers are negative, it correctly identifies the highest value. " +
            "E.g.: [-5, -2, -10] returns -2")
    @Order(4)
    void testAllNegativeNumbers() {
        // This test will fail if the learner initialized `int max = 0;`
        verifyBasicKata(new FindMaximumValueKata(),
                List.of(-5, -2, -10, -3),
                -2,
                Objects::equals);
    }

    @Test
    @DisplayName("When the list contains only a single element, returns that element. " +
            "E.g.: [7] returns 7")
    @Order(5)
    void testSingleElement() {
        verifyBasicKata(new FindMaximumValueKata(),
                List.of(7),
                7,
                Objects::equals);
    }
}
