package io.code.vanguard.brew.list;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@Tag("List")
@DisplayName("List - Remove Duplicates")
public class RemoveDuplicatesTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a list with adjacent duplicates, it keeps only the first occurrence. " +
            "E.g.: [1, 2, 2, 3] returns [1, 2, 3]")
    @Order(1)
    void testAdjacentDuplicates() {
        verifyBasicKata(new RemoveDuplicatesKata(),
                List.of(1, 2, 2, 3),
                List.of(1, 2, 3),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a list with non-adjacent duplicates, it still removes the extras. " +
            "E.g.: [1, 2, 1, 3, 2] returns [1, 2, 3]")
    @Order(2)
    void testNonAdjacentDuplicates() {
        verifyBasicKata(new RemoveDuplicatesKata(),
                List.of(1, 2, 1, 3, 2),
                List.of(1, 2, 3),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a list made entirely of the same number, returns a list with just one element. " +
            "E.g.: [9, 9, 9, 9] returns [9]")
    @Order(3)
    void testAllDuplicates() {
        verifyBasicKata(new RemoveDuplicatesKata(),
                List.of(9, 9, 9, 9, 9),
                List.of(9),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a list with no duplicates, returns a list identical to the input. " +
            "E.g.: [5, 6, 7] returns [5, 6, 7]")
    @Order(4)
    void testNoDuplicates() {
        verifyBasicKata(new RemoveDuplicatesKata(),
                List.of(5, 6, 7),
                List.of(5, 6, 7),
                List::equals);
    }
}
