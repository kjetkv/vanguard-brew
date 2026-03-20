package io.code.vanguard.brew.set;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

@Tag("Set")
@DisplayName("Set - The Merger (Set Union)")
public class SetUnionTest extends BasicKataTestBase {

    @Test
    @DisplayName("When providing lists with cross-list duplicates, merges them into a single unique set. " +
            "E.g.: [[A, B], [B, C]] returns {A, B, C}")
    @Order(1)
    void testCrossListDuplicates() {
        verifyBasicKata(new SetUnionKata(),
                List.of(List.of("A", "B"), List.of("B", "C")),
                Set.of("A", "B", "C"),
                Set::equals);
    }

    @Test
    @DisplayName("When providing lists that contain internal duplicates, deduplicates them perfectly. " +
            "E.g.: [[X, X, Y], [Z, Z]] returns {X, Y, Z}")
    @Order(2)
    void testInternalDuplicates() {
        verifyBasicKata(new SetUnionKata(),
                List.of(List.of("X", "X", "Y"), List.of("Z", "Z")),
                Set.of("X", "Y", "Z"),
                Set::equals);
    }

    @Test
    @DisplayName("When providing completely disjoint lists, combines all elements successfully. " +
            "E.g.: [[1, 2], [3, 4], [5, 6]] returns {1, 2, 3, 4, 5, 6}")
    @Order(3)
    void testDisjointLists() {
        verifyBasicKata(new SetUnionKata(),
                List.of(List.of("1", "2"), List.of("3", "4"), List.of("5", "6")),
                Set.of("1", "2", "3", "4", "5", "6"),
                Set::equals);
    }

    @Test
    @DisplayName("When a list is empty among populated lists, it merges the populated ones without issue. " +
            "E.g.: [[Apple], [], [Banana]] returns {Apple, Banana}")
    @Order(4)
    void testEmptyListInTheMiddle() {
        verifyBasicKata(new SetUnionKata(),
                List.of(List.of("Apple"), List.of(), List.of("Banana")),
                Set.of("Apple", "Banana"),
                Set::equals);
    }

    @Test
    @DisplayName("When the input is empty or contains only empty lists, returns an empty set. " +
            "E.g.: [[], []] returns {}")
    @Order(5)
    void testAllEmptyLists() {
        verifyBasicKata(new SetUnionKata(),
                List.of(List.of(), List.of()),
                Set.of(),
                Set::equals);
    }
}
