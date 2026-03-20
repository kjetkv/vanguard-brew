package io.code.vanguard.brew.constructing;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@DisplayName("Constructing - The Caching Highlander (Static Factory)")
@Tag("Constructing")
public class CachingHighlanderTest extends BasicKataTestBase {

    @Test
    @DisplayName("When requesting a connection for the same URL, returns the exact same instance in memory.")
    @Order(1)
    void testSameUrlReturnsSameInstance() {
        CachingHighlanderKata originalInstance = CachingHighlanderKata.getInstance("db://main-cluster");

        verifyClass(
                originalInstance,
                conn -> CachingHighlanderKata.getInstance("db://main-cluster"),
                originalInstance,
                (expected, actual) -> expected == actual
        );
    }

    @Test
    @DisplayName("When requesting a connection for a different URL, returns a distinctly new instance.")
    @Order(2)
    void testDifferentUrlReturnsDifferentInstance() {
        CachingHighlanderKata originalInstance = CachingHighlanderKata.getInstance("db://replica-a");

        verifyClass(
                originalInstance,
                conn -> CachingHighlanderKata.getInstance("db://replica-b"),
                originalInstance,
                (expected, actual) -> expected != actual
        );
    }

    @Test
    @DisplayName("When the URL is null, throws an IllegalArgumentException.")
    @Order(3)
    void testNullUrlThrowsException() {
        verifyException(
                () -> CachingHighlanderKata.getInstance(null),
                new IllegalArgumentException()
        );
    }

    @Test
    @DisplayName("When the URL is blank, throws an IllegalArgumentException.")
    @Order(4)
    void testBlankUrlThrowsException() {
        verifyException(
                () -> CachingHighlanderKata.getInstance("   "),
                new IllegalArgumentException()
        );
    }
}
