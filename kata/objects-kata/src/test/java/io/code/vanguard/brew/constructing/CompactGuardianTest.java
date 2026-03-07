package io.code.vanguard.brew.constructing;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Validators.verifySameExceptionClass;

@DisplayName("Constructing - Compact Guardian (Records)")
@Tag("Constructing")
public class CompactGuardianTest extends BasicKataTestBase {

    @Test
    @DisplayName("When providing valid credentials, successfully creates the record.")
    @Order(1)
    void testValidCredentials() {
        verify(
                new CompactGuardianKata("admin_user", "SecurePass123!"),
                compactGuardianKata -> {
                    //Do nothing
                },
                CompactGuardianKata::username,
                "admin_user",
                Objects::equals
        );
    }

    @Test
    @DisplayName("When the username is blank, throws an IllegalArgumentException.")
    @Order(2)
    void testBlankUsernameThrowsException() {
        verify(
                () -> new CompactGuardianKata("   ", "SecurePass123!"),
                new IllegalArgumentException(),
                verifySameExceptionClass
        );
    }

    @Test
    @DisplayName("When the username is null, throws an IllegalArgumentException.")
    @Order(3)
    void testNullUsernameThrowsException() {
        verify(
                () -> new CompactGuardianKata(null, "SecurePass123!"),
                new IllegalArgumentException(),
                verifySameExceptionClass
        );
    }

    @Test
    @DisplayName("When the password is less than 8 characters, throws an IllegalArgumentException.")
    @Order(4)
    void testShortPasswordThrowsException() {
        verify(
                () -> new CompactGuardianKata("admin_user", "short"),
                new IllegalArgumentException(),
                verifySameExceptionClass
        );
    }

    @Test
    @DisplayName("When the password is null, throws an IllegalArgumentException.")
    @Order(5)
    void testNullPasswordThrowsException() {
        verify(
                () -> new CompactGuardianKata("admin_user", null),
                new IllegalArgumentException(),
                verifySameExceptionClass
        );
    }
}