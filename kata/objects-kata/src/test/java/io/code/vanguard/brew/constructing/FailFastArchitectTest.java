package io.code.vanguard.brew.constructing;


import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Validators.verifySameExceptionClass;
import static io.code.vanguard.brew.constructing.FailFastArchitectKata.SmartSystemInitializer;

@DisplayName("Constructing - Fail-Fast Architect")
@Tag("Constructing")
public class FailFastArchitectTest extends BasicKataTestBase {

    @Test
    @DisplayName("When providing a perfectly formatted payload, successfully parses and initializes the superclass.")
    @Order(1)
    void testSuccessfulInitialization() {
        verify(
                new SmartSystemInitializer("1042:ADMIN"),
                system -> {
                    //Do nothing
                },
                FailFastArchitectKata::getStatus,
                "System 1042 initialized as ADMIN",
                Objects::equals
        );
    }

    @Test
    @DisplayName("When providing a payload missing the delimiter, throws an IllegalArgumentException")
    @Order(2)
    void testMissingDelimiterFailFast() {
        verify(
                () -> new SmartSystemInitializer("1042ADMIN"),
                new IllegalArgumentException(),
                verifySameExceptionClass
        );
    }

    @Test
    @DisplayName("When providing a payload with an ID below 1000, throws an IllegalArgumentException.")
    @Order(3)
    void testInvalidIdFailFast() {
        verify(
                () -> new SmartSystemInitializer("999:USER"),
                new IllegalArgumentException(),
                verifySameExceptionClass
        );
    }

    @Test
    @DisplayName("When providing a payload with too many segments, throws an IllegalArgumentException.")
    @Order(4)
    void testMalformedFormatFailFast() {
        verify(
                () -> new SmartSystemInitializer("1042:ADMIN:EXTRA"),
                new IllegalArgumentException(),
                verifySameExceptionClass
        );
    }

    @Test
    @DisplayName("When providing a completely null payload, throws an IllegalArgumentException.")
    @Order(5)
    void testNullPayloadFailFast() {
        verify(
                () -> new SmartSystemInitializer(null),
                new IllegalArgumentException(),
                verifySameExceptionClass
        );
    }
}