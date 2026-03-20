package io.code.vanguard.brew.regex;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Extractors.regexGroupsAsString;

@DisplayName("String - Bot Command Extractor")
@Tag("String")
public class BotCommandRegexTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the text starts exactly with a command, captures the command. " +
            "E.g.: '/dance now' returns '/dance'")
    @Order(1)
    void testCommandAtStart() {
        verifyNoArgKata(new BotCommandRegexKata(),
                "/start",
                regex -> regexGroupsAsString.apply(regex, "/start the game please."),
                Objects::equals);
    }

    @Test
    @DisplayName("When the command is hidden in the middle of a sentence, ignores it completely. " +
            "E.g.: 'I need /help' returns ''")
    @Order(2)
    void testCommandInMiddle() {
        verifyNoArgKata(new BotCommandRegexKata(),
                "",
                regex -> regexGroupsAsString.apply(regex, "Hey everyone, type /join to enter."),
                Objects::equals);
    }

    @Test
    @DisplayName("When the text contains multiple commands, only captures the one at the very beginning. " +
            "E.g.: '/roll and /stats' returns '/roll'")
    @Order(3)
    void testMultipleCommands() {
        verifyNoArgKata(new BotCommandRegexKata(),
                "/kick",
                regex -> regexGroupsAsString.apply(regex, "/kick user1 and /ban user2"),
                Objects::equals);
    }

    @Test
    @DisplayName("When the text starts with a slash but no word characters, returns nothing. " +
            "E.g.: '/ 123' returns ''")
    @Order(4)
    void testInvalidCommandFormat() {
        verifyNoArgKata(new BotCommandRegexKata(),
                "",
                regex -> regexGroupsAsString.apply(regex, "/ ? what is this"),
                Objects::equals);
    }

    @Test
    @DisplayName("When the text has spaces before the command, ignores it because it is not at the absolute start. " +
            "E.g.: ' /ping' returns ''")
    @Order(5)
    void testSpaceBeforeCommand() {
        verifyNoArgKata(new BotCommandRegexKata(),
                "",
                regex -> regexGroupsAsString.apply(regex, "   /status"),
                Objects::equals);
    }
}