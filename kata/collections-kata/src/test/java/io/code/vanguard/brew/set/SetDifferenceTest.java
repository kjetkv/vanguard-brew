package io.code.vanguard.brew.set;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

@Tag("Set")
@DisplayName("Set - The Missing Pieces (Set Difference)")
public class SetDifferenceTest extends BasicKataTestBase {

    @Test
    @DisplayName("When providing two lists, removes elements of the second from the first. " +
            "E.g.: [[1, 2, 3], [2, 3, 4]] returns {1}")
    @Order(1)
    void testTwoListDifference() {
        verifyBasicKata(new SetDifferenceKata(),
                List.of(List.of(1, 2, 3), List.of(2, 3, 4)),
                Set.of(1),
                Set::equals);
    }

    @Test
    @DisplayName("When providing three lists, sequentially removes elements of subsequent lists from the first. " +
            "E.g.: [[10, 20, 30, 40], [20], [40, 50]] returns {10, 30}")
    @Order(2)
    void testThreeListDifference() {
        verifyBasicKata(new SetDifferenceKata(),
                List.of(List.of(10, 20, 30, 40), List.of(20), List.of(40, 50)),
                Set.of(10, 30),
                Set::equals);
    }

    @Test
    @DisplayName("When subsequent lists contain all elements of the first list, returns an empty set. " +
            "E.g.: [[1, 2], [1], [2]] returns {}")
    @Order(3)
    void testCompleteAnnihilation() {
        verifyBasicKata(new SetDifferenceKata(),
                List.of(List.of(1, 2), List.of(1), List.of(2)),
                Set.of(),
                Set::equals);
    }

    @Test
    @DisplayName("When there is absolutely no overlap, the base set remains completely unchanged. " +
            "E.g.: [[1, 2], [3, 4], [5, 6]] returns {1, 2}")
    @Order(4)
    void testNoOverlapUnchanged() {
        verifyBasicKata(new SetDifferenceKata(),
                List.of(List.of(1, 2), List.of(3, 4), List.of(5, 6)),
                Set.of(1, 2),
                Set::equals);
    }

    @Test
    @DisplayName("When the very first list is empty, there is nothing to subtract from, returning an empty set. " +
            "E.g.: [[], [1, 2, 3]] returns {}")
    @Order(5)
    void testFirstListEmpty() {
        verifyBasicKata(new SetDifferenceKata(),
                List.of(List.of(), List.of(1, 2, 3)),
                Set.of(),
                Set::equals);
    }
}