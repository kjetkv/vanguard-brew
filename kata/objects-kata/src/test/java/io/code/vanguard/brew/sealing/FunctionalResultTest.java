package io.code.vanguard.brew.sealing;

import io.code.vanguard.brew.BasicKataTestBase;
import io.code.vanguard.brew.sealing.FunctionalResultKata.Failure;
import io.code.vanguard.brew.sealing.FunctionalResultKata.Success;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Extractors.parentInterfaceForClass;
import static io.code.vanguard.brew.sealing.FunctionalResultKata.Result;

@DisplayName("Sealing - Functional Result")
@Tag("Sealing")
public class FunctionalResultTest extends BasicKataTestBase {

    @Test
    @DisplayName("Result must be a sealed interface.")
    @Order(1)
    void testResultIsSealed() {
        verifyClass(
                Result.class,
                Class::isSealed,
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Success must be implemented as a Java Record.")
    @Order(2)
    void testSuccessIsRecord() {
        verifyClass(
                Success.class,
                Class::isRecord,
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Failure must be implemented as a Java Record.")
    @Order(3)
    void testFailureIsRecord() {
        verifyClass(
                Failure.class,
                Class::isRecord,
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Success must implement Result.")
    @Order(4)
    void testSuccessInheritance() {
        verifyClass(
                Success.class,
                parentInterfaceForClass,
                "Parent-interface: Result",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Failure must implement Result.")
    @Order(5)
    void testFailureInheritance() {
        verifyClass(
                Failure.class,
                parentInterfaceForClass,
                "Parent-interface: Result",
                Objects::equals
        );
    }

    @Test
    @DisplayName("When providing a Success, extracts the data perfectly.")
    @Order(6)
    void testSuccessProcessing() {
        verifyBasicKata(
                new FunctionalResultKata(),
                new Success("200 OK"),
                "Processed Data: 200 OK",
                Objects::equals
        );
    }

    @Test
    @DisplayName("When providing a Failure, extracts the error message perfectly.")
    @Order(7)
    void testFailureProcessing() {
        verifyBasicKata(
                new FunctionalResultKata(),
                new Failure<>("Connection Timeout"),
                "Operation Failed: Connection Timeout",
                Objects::equals
        );
    }

    @Test
    @DisplayName("When providing a null result, natively throws a NullPointerException.")
    @Order(8)
    void testNullResultThrowsNPE() {
        verifyException(
                () -> new FunctionalResultKata().solve(null),
                new NullPointerException()
        );
    }
}