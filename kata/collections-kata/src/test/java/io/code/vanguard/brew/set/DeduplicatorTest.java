package io.code.vanguard.brew.set;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

@Tag("Set")
@DisplayName("Set - The Deduplicator (List to Set)")
public class DeduplicatorTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a list with adjacent duplicates, returns a Set with only unique values. " +
            "E.g.: [1, 2, 2, 3] returns [1, 2, 3]")
    @Order(1)
    void testStandardDeduplication() {
        verifyBasicKata(new DeduplicatorKata(),
                List.of(1, 2, 2, 3),
                Set.of(1, 2, 3),
                Set::equals);
    }

    @Test
    @DisplayName("When receiving a list with non-adjacent duplicates, handles them perfectly. " +
            "E.g.: [5, 1, 5, 2, 5] returns [1, 2, 5]")
    @Order(2)
    void testScatteredDuplicates() {
        verifyBasicKata(new DeduplicatorKata(),
                List.of(5, 1, 5, 2, 5),
                Set.of(1, 2, 5),
                Set::equals);
    }

    @Test
    @DisplayName("When receiving a list made entirely of the same number, returns a Set with exactly one element. " +
            "E.g.: [9, 9, 9, 9] returns [9]")
    @Order(3)
    void testAllIdenticalNumbers() {
        verifyBasicKata(new DeduplicatorKata(),
                List.of(9, 9, 9, 9, 9),
                Set.of(9),
                Set::equals);
    }

    @Test
    @DisplayName("When receiving a list that already has no duplicates, returns a Set with all the elements. " +
            "E.g.: [4, 5, 6] returns [4, 5, 6]")
    @Order(4)
    void testNoDuplicatesToBeginWith() {
        verifyBasicKata(new DeduplicatorKata(),
                List.of(4, 5, 6),
                Set.of(4, 5, 6),
                Set::equals);
    }

/*    @Test
    @DisplayName("When receiving a list with null elements or an entirely null list, returns an empty Set safely. " +
            "E.g.: null returns []")
    @Order(5)
    void testNullHandling() {
        verify(new DeduplicatorKata(),
                null,
                Set.of(),
                Set::equals);
    }*/
}
