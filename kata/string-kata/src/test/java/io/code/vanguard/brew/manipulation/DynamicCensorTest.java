package io.code.vanguard.brew.manipulation;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

@DisplayName("String - Dynamic Censor")
@Tag("String")
public class DynamicCensorTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the bad word is present, replaces it with the exact number of asterisks. " +
            "E.g.: 'I hate bugs' (badWord: 'bugs') returns 'I hate ****'")
    @Order(1)
    void testStandardCensorship() {
        verifyBasicKata(new DynamicCensorKata(),
                new DynamicCensorKata.CensorRequest("Voldemort has returned!", "Voldemort"),
                "********* has returned!",
                Objects::equals);
    }

    @Test
    @DisplayName("When the bad word appears multiple times, censors every single instance. " +
            "E.g.: 'No no no!' (badWord: 'no') returns '** ** **!'")
    @Order(2)
    void testMultipleOccurrences() {
        verifyBasicKata(new DynamicCensorKata(),
                new DynamicCensorKata.CensorRequest("Daleks say exterminate, EXTERMINATE!", "exterminate"),
                "Daleks say ***********, ***********!",
                Objects::equals);
    }

    @Test
    @DisplayName("When the bad word has mixed casing, the censor catches it case-insensitively. " +
            "E.g.: 'BaD wOrD' (badWord: 'bad') returns '*** wOrD'")
    @Order(3)
    void testCaseInsensitivity() {
        verifyBasicKata(new DynamicCensorKata(),
                new DynamicCensorKata.CensorRequest("Do you bleed? You will BLEED.", "bleed"),
                "Do you *****? You will *****.",
                Objects::equals);
    }

    @Test
    @DisplayName("When the bad word is not in the text, returns the original string unchanged. " +
            "E.g.: 'Hello world' (badWord: 'bye') returns 'Hello world'")
    @Order(4)
    void testWordNotFound() {
        verifyBasicKata(new DynamicCensorKata(),
                new DynamicCensorKata.CensorRequest("These are not the droids you are looking for.", "Jedi"),
                "These are not the droids you are looking for.",
                Objects::equals);
    }

    @Test
    @DisplayName("When the bad word contains special characters, handles them safely without errors. " +
            "E.g.: 'Error $100!' (badWord: '$100') returns 'Error ****!'")
    @Order(5)
    void testSpecialCharactersInBadWord() {
        verifyBasicKata(new DynamicCensorKata(),
                new DynamicCensorKata.CensorRequest("Wait a minute, Doc. Are you telling me you built a time machine?", "Doc."),
                "Wait a minute, **** Are you telling me you built a time machine?",
                Objects::equals);
    }
}