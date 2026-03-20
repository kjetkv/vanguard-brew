package io.code.vanguard.brew.list;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@Tag("List")
@DisplayName("List - Merge Lists")
public class MergeListsTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a list containing two populated lists, returns a single merged list. " +
            "E.g.: [[1, 2], [3, 4]] returns [1, 2, 3, 4]")
    @Order(1)
    void testMergeTwoPopulatedLists() {
        verifyBasicKata(new MergeListsKata(),
                List.of(List.of(1, 2), List.of(3, 4)),
                List.of(1, 2, 3, 4),
                List::equals);
    }

    @Test
    @DisplayName("When the first inner list is empty, returns only the elements of the second list. " +
            "E.g.: [[], [5, 6]] returns [5, 6]")
    @Order(2)
    void testFirstListEmpty() {
        verifyBasicKata(new MergeListsKata(),
                List.of(List.of(), List.of(5, 6)),
                List.of(5, 6),
                List::equals);
    }

    @Test
    @DisplayName("When the second inner list is empty, returns only the elements of the first list. " +
            "E.g.: [[7, 8], []] returns [7, 8]")
    @Order(3)
    void testSecondListEmpty() {
        verifyBasicKata(new MergeListsKata(),
                List.of(List.of(7, 8), List.of()),
                List.of(7, 8),
                List::equals);
    }

    @Test
    @DisplayName("When receiving more than two lists, it successfully merges all of them. " +
            "E.g.: [[1], [2], [3]] returns [1, 2, 3]")
    @Order(4)
    void testMergeMultipleLists() {
        verifyBasicKata(new MergeListsKata(),
                List.of(List.of(1), List.of(2), List.of(3)),
                List.of(1, 2, 3),
                List::equals);
    }

/*    @Test
    @DisplayName("When receiving a null input or a list containing nulls, safely returns an empty or partial list. " +
            "E.g.: null returns []")
    @Order(5)
    void testNullHandling() {
        verify(new MergeListsKata(),
                null,
                List.of(),
                List::equals);
    }*/
}