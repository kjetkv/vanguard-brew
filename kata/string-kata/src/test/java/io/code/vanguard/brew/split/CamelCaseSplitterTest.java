package io.code.vanguard.brew.split;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("String - CamelCase Splitter")
@Tag("String")
public class CamelCaseSplitterTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a standard camelCase string, splits it at the capital letters. " +
            "E.g.: 'myVariableName' returns ['my', 'Variable', 'Name']")
    @Order(1)
    void testStandardCamelCase() {
        verifyBasicKata(new CamelCaseSplitterKata(),
                "teenageMutantNinjaTurtles",
                List.of("teenage", "Mutant", "Ninja", "Turtles"),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a PascalCase string, keeps the first capital letter intact with the first word. " +
            "E.g.: 'FirstName' returns ['First', 'Name']")
    @Order(2)
    void testPascalCase() {
        verifyBasicKata(new CamelCaseSplitterKata(),
                "TheLordOfTheRings",
                List.of("The", "Lord", "Of", "The", "Rings"),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a long camelCase string, splits every word correctly. " +
            "E.g.: 'oneTwoThreeFour' returns ['one', 'Two', 'Three', 'Four']")
    @Order(3)
    void testLongCamelCase() {
        verifyBasicKata(new CamelCaseSplitterKata(),
                "harryPotterAndTheSorcerersStoneThatFellOfTheWagon",
                List.of("harry", "Potter", "And", "The", "Sorcerers", "Stone", "That", "Fell", "Of", "The", "Wagon"),
                List::equals);
    }

    @Test
    @DisplayName("When receiving a single fully lowercase word, returns a list with just that word. " +
            "E.g.: 'hello' returns ['hello']")
    @Order(4)
    void testSingleLowercaseWord() {
        verifyBasicKata(new CamelCaseSplitterKata(),
                "gandalf",
                List.of("gandalf"),
                List::equals);
    }

    @Test
    @DisplayName("When receiving an empty string, safely returns an empty list. " +
            "E.g.: '' returns []")
    @Order(5)
    void testEmptyString() {
        verifyBasicKata(new CamelCaseSplitterKata(),
                "",
                List.of(),
                List::equals);
    }
}