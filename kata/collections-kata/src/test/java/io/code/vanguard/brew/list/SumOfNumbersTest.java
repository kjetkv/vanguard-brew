package io.code.vanguard.brew.list;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

@Tag("List")
@DisplayName("List - Sum of Numbers")
public class SumOfNumbersTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a list of positive integers, returns their total sum. " +
            "E.g.: [1, 2, 3] returns 6")
    @Order(1)
    void testSumPositiveNumbers() {
        verifyBasicKata(new SumOfNumbersKata(),
                List.of(1, 2, 3),
                6,
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving a list containing negative integers, adds them correctly to the sum. " +
            "E.g.: [5, -2, 3] returns 6")
    @Order(2)
    void testSumWithNegativeNumbers() {
        verifyBasicKata(new SumOfNumbersKata(),
                List.of(5, -2, 3),
                6,
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving a list of zeros, returns zero. " +
            "E.g.: [0, 0, 0] returns 0")
    @Order(3)
    void testSumOfZeros() {
        verifyBasicKata(new SumOfNumbersKata(),
                List.of(0, 0, 0),
                0,
                Objects::equals);
    }

    @Test
    @DisplayName("When receiving an empty list, returns zero. " +
            "E.g.: [] returns 0")
    @Order(4)
    void testEmptyList() {
        verifyBasicKata(new SumOfNumbersKata(),
                List.of(),
                0,
                Objects::equals);
    }
}
