package io.code.vanguard.brew.regex;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

@DisplayName("String - Bouncer (Email Validator)")
@Tag("String")
public class EmailValidatorTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a perfectly formatted email, returns true.")
    @Order(1)
    void testValidEmail() {
        verifyBasicKata(new EmailValidatorKata(),
                "tony.stark@avengers.com",
                true,
                Objects::equals);
    }

    @Test
    @DisplayName("When the email is missing the @ symbol, returns false.")
    @Order(2)
    void testMissingAtSymbol() {
        verifyBasicKata(new EmailValidatorKata(),
                "peter.parker.dailybugle.net",
                false,
                Objects::equals);
    }

    @Test
    @DisplayName("When the email is missing the domain extension (the dot), returns false.")
    @Order(3)
    void testMissingDomainExtension() {
        verifyBasicKata(new EmailValidatorKata(),
                "bruce.wayne@batcave",
                false,
                Objects::equals);
    }

    @Test
    @DisplayName("When the email contains illegal spaces, returns false.")
    @Order(4)
    void testIllegalSpaces() {
        verifyBasicKata(new EmailValidatorKata(),
                "clark kent@dailyplanet.com",
                false,
                Objects::equals);
    }

    @Test
    @DisplayName("When the email has multiple @ symbols, returns false.")
    @Order(5)
    void testMultipleAtSymbols() {
        verifyBasicKata(new EmailValidatorKata(),
                "neo@matrix@zion.org",
                false,
                Objects::equals);
    }
}