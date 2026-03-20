package io.code.vanguard.brew.manipulation;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

@DisplayName("String - Search Highlighter")
@Tag("String")
public class SearchHighlighterTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the search term is found, wraps it perfectly in mark tags. " +
            "E.g.: 'Look here' (term: 'here') returns 'Look [MARK]here[/MARK]'")
    @Order(1)
    void testStandardHighlight() {
        verifyBasicKata(new SearchHighlighterKata(),
                new SearchHighlighterKata.HighlightRequest("The cake is a lie.", "cake"),
                "The [MARK]cake[/MARK] is a lie.",
                Objects::equals);
    }

    @Test
    @DisplayName("When the term appears multiple times, wraps every instance. " +
            "E.g.: 'A cat and another cat' (term: 'cat') returns 'A [MARK]cat[/MARK] and another [MARK]cat[/MARK]'")
    @Order(2)
    void testMultipleHighlights() {
        verifyBasicKata(new SearchHighlighterKata(),
                new SearchHighlighterKata.HighlightRequest("I am Groot. We are Groot.", "Groot"),
                "I am [MARK]Groot[/MARK]. We are [MARK]Groot[/MARK].",
                Objects::equals);
    }

    @Test
    @DisplayName("When matching case-insensitively, preserves the original casing of the matched text inside the tags. " +
            "E.g.: 'BIG big' (term: 'big') returns '[MARK]BIG[/MARK] [MARK]big[/MARK]'")
    @Order(3)
    void testPreservesOriginalCasing() {
        verifyBasicKata(new SearchHighlighterKata(),
                new SearchHighlighterKata.HighlightRequest("Bears. Beets. Battlestar Galactica.", "bear"),
                "[MARK]Bear[/MARK]s. Beets. Battlestar Galactica.",
                Objects::equals);
    }

    @Test
    @DisplayName("When the term is part of a larger word, highlights just that portion. " +
            "E.g.: 'Understand' (term: 'under') returns '[MARK]Under[/MARK]stand'")
    @Order(4)
    void testPartialWordHighlight() {
        verifyBasicKata(new SearchHighlighterKata(),
                new SearchHighlighterKata.HighlightRequest("Wakanda Forever!", "kan"),
                "Wa[MARK]kan[/MARK]da Forever!",
                Objects::equals);
    }

    @Test
    @DisplayName("When the search term is not found, returns the exact original string. " +
            "E.g.: 'Nothing to see' (term: 'hidden') returns 'Nothing to see'")
    @Order(5)
    void testTermNotFound() {
        verifyBasicKata(new SearchHighlighterKata(),
                new SearchHighlighterKata.HighlightRequest("Yer a wizard, Harry.", "Muggle"),
                "Yer a wizard, Harry.",
                Objects::equals);
    }
}
