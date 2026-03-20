package io.code.vanguard.brew.regex;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Extractors.regexGroupsAsString;

@DisplayName("String - PIN Code Extractor")
@Tag("String")
public class PinCodeRegexTest extends BasicKataTestBase {

    @Test
    @DisplayName("When the text contains a standard 4-digit number, captures it. " +
            "E.g.: 'Code is 1234' returns '1234'")
    @Order(1)
    void testFourDigitPin() {
        verifyNoArgKata(new PinCodeRegexKata(),
                "9876",
                regex -> regexGroupsAsString.apply(regex, "Your temporary access PIN is 9876."),
                Objects::equals);
    }

    @Test
    @DisplayName("When the text contains a 6-digit number, captures it. " +
            "E.g.: 'Auth code 123456' returns '123456'")
    @Order(2)
    void testSixDigitPin() {
        verifyNoArgKata(new PinCodeRegexKata(),
                "555666",
                regex -> regexGroupsAsString.apply(regex, "Enter the auth token 555666 to proceed."),
                Objects::equals);
    }

    @Test
    @DisplayName("When the text contains numbers shorter than 4 digits, ignores them completely. " +
            "E.g.: 'Room 101' returns ''")
    @Order(3)
    void testPinTooShort() {
        verifyNoArgKata(new PinCodeRegexKata(),
                "",
                regex -> regexGroupsAsString.apply(regex, "Agent 007, your mission begins now."),
                Objects::equals);
    }

    @Test
    @DisplayName("When the text contains numbers longer than 6 digits, ignores them without extracting partial matches. " +
            "E.g.: 'Call 5551234' returns ''")
    @Order(4)
    void testPinTooLong() {
        verifyNoArgKata(new PinCodeRegexKata(),
                "",
                regex -> regexGroupsAsString.apply(regex, "My phone number is 8675309."),
                Objects::equals);
    }

    @Test
    @DisplayName("When multiple valid and invalid numbers are mixed, extracts only the valid PINs in order. " +
            "E.g.: '12, 1234, 1234567' returns '1234'")
    @Order(5)
    void testMixedLengthNumbers() {
        verifyNoArgKata(new PinCodeRegexKata(),
                "1024,80808",
                regex -> regexGroupsAsString.apply(regex, "Ports 80 and 443 are open. Proxies at 1024 and 80808."),
                Objects::equals);
    }
}
