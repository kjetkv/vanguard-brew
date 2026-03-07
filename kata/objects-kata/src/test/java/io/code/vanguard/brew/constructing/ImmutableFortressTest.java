package io.code.vanguard.brew.constructing;

import io.code.vanguard.brew.BasicKataTestBase;
import io.code.vanguard.brew.constructing.ImmutableFortressKata.Builder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Optional;

import static io.code.vanguard.brew.Validators.verifySameExceptionClass;

@DisplayName("Constructing - Immutable Fortress")
@Tag("Constructing")
public class ImmutableFortressTest extends BasicKataTestBase {

    @Test
    @DisplayName("When all properties are chained fluently, successfully builds the configuration.")
    @Order(1)
    void testFullBuilderFlow() {
        verify(
                new ImmutableFortressKata.Builder(),
                builder -> builder.host("api.foo.com")
                                  .port(443)
                                  .useHttps(true)
                                  .maxConnections(500),
                builder -> Optional.of(builder)
                                   .map(Builder::build)
                                   .map(ImmutableFortressKata::toString)
                                   .orElse(null),
                "Server[host='api.foo.com', port=443, https=true, maxConn=500]",
                Objects::equals
        );
    }

    @Test
    @DisplayName("When optional properties are omitted, the builder applies the correct defaults.")
    @Order(2)
    void testBuilderDefaults() {
        verify(
                new ImmutableFortressKata.Builder(),
                builder -> builder.host("localhost")
                                  .port(8080),
                builder -> Optional.of(builder)
                                   .map(Builder::build)
                                   .map(ImmutableFortressKata::toString)
                                   .orElse(null),
                "Server[host='localhost', port=8080, https=false, maxConn=10]",
                Objects::equals
        );
    }

    @Test
    @DisplayName("When the mandatory 'host' field is missing, build() throws an IllegalStateException.")
    @Order(3)
    void testMissingHostThrowsException() {
        verify(
                () -> new ImmutableFortressKata.Builder().port(8080)
                                                         .build(),
                new IllegalStateException(),
                verifySameExceptionClass
        );
    }

    @Test
    @DisplayName("When the mandatory 'port' field is missing, build() throws an IllegalStateException.")
    @Order(4)
    void testMissingPortThrowsException() {
        verify(
                () -> new ImmutableFortressKata.Builder().host("localhost")
                                                         .build(),
                new IllegalStateException(),
                verifySameExceptionClass
        );
    }
}
