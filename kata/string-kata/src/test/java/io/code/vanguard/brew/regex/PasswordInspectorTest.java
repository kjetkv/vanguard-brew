package io.code.vanguard.brew.regex;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

@DisplayName("String - Password Inspector")
@Tag("String")
public class PasswordInspectorTest extends BasicKataTestBase {

    @Test
    @DisplayName("When receiving a password that meets all criteria, returns true. " +
            "E.g.: 'Valid123!' returns true")
    @Order(1)
    void testPerfectPassword() {
        verifyBasicKata(new PasswordInspectorKata(),
                "Triforce!1986",
                true,
                Objects::equals);
    }

    @Test
    @DisplayName("When the password is too short (less than 8 chars) but meets all other criteria, returns false. " +
            "E.g.: 'A1!bcd' returns false")
    @Order(2)
    void testTooShort() {
        verifyBasicKata(new PasswordInspectorKata(),
                "P@ss1",
                false,
                Objects::equals);
    }

    @Test
    @DisplayName("When the password lacks an uppercase letter, returns false. " +
            "E.g.: 'lowercase1!' returns false")
    @Order(3)
    void testNoUppercase() {
        verifyBasicKata(new PasswordInspectorKata(),
                "mischief_managed_99",
                false,
                Objects::equals);
    }

    @Test
    @DisplayName("When the password lacks a number, returns false. " +
            "E.g.: 'NoNumbersHere!' returns false")
    @Order(4)
    void testNoNumber() {
        verifyBasicKata(new PasswordInspectorKata(),
                "WinterIsComing!!!",
                false,
                Objects::equals);
    }

    @Test
    @DisplayName("When the password lacks a special character, returns false. " +
            "E.g.: 'NoSpecialChars123' returns false")
    @Order(5)
    void testNoSpecialCharacter() {
        verifyBasicKata(new PasswordInspectorKata(),
                "GottaCatchEmAll151",
                false,
                Objects::equals);
    }
}