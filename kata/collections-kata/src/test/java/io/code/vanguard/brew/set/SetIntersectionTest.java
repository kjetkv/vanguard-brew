package io.code.vanguard.brew.set;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

@Tag("Set")
@DisplayName("Set - The Common Ground (Intersection of Multiple Lists)")
public class SetIntersectionTest extends BasicKataTestBase {

    @Test
    @DisplayName("When providing two lists with a partial overlap, returns only the shared elements. " +
            "E.g.: [[A, B, C], [B, C, D]] returns {B, C}")
    @Order(1)
    void testTwoListsOverlap() {
        verifyBasicKata(new SetIntersectionKata(),
                List.of(List.of("A", "B", "C"), List.of("B", "C", "D")),
                Set.of("B", "C"),
                Set::equals);
    }

    @Test
    @DisplayName("When providing three lists, returns only the elements present in ALL of them. " +
            "E.g.: [[A, B], [B, C], [B, D]] returns {B}")
    @Order(2)
    void testThreeListsOverlap() {
        verifyBasicKata(new SetIntersectionKata(),
                List.of(List.of("A", "B"), List.of("B", "C"), List.of("B", "D")),
                Set.of("B"),
                Set::equals);
    }

    @Test
    @DisplayName("When items are shared between some lists but not ALL lists, returns an empty set. " +
            "E.g.: [[A, B], [B, C], [C, D]] returns {}")
    @Order(3)
    void testNoUniversalOverlap() {
        verifyBasicKata(new SetIntersectionKata(),
                List.of(List.of("A", "B"), List.of("B", "C"), List.of("C", "D")),
                Set.of(),
                Set::equals);
    }

    @Test
    @DisplayName("When providing multiple identical lists, returns all elements from those lists. " +
            "E.g.: [[X, Y], [X, Y], [X, Y]] returns {X, Y}")
    @Order(4)
    void testAllIdenticalLists() {
        verifyBasicKata(new SetIntersectionKata(),
                List.of(List.of("X", "Y"), List.of("X", "Y"), List.of("X", "Y")),
                Set.of("X", "Y"),
                Set::equals);
    }

    @Test
    @DisplayName("When even a single list is empty, the universal common ground becomes empty. " +
            "E.g.: [[A, B], [], [A, B]] returns {}")
    @Order(5)
    void testOneEmptyListRuinsIt() {
        verifyBasicKata(new SetIntersectionKata(),
                List.of(List.of("A", "B"), List.of(), List.of("A", "B")),
                Set.of(),
                Set::equals);
    }
}