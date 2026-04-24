package io.code.vanguard.brew.statemachine;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

import static io.code.vanguard.brew.statemachine.AuditLogKata.AuditCrashException;
import static io.code.vanguard.brew.statemachine.AuditLogKata.Event;
import static io.code.vanguard.brew.statemachine.AuditLogKata.Event.BREW;
import static io.code.vanguard.brew.statemachine.AuditLogKata.Event.CLEAN;
import static io.code.vanguard.brew.statemachine.AuditLogKata.Event.POWER_OFF;
import static io.code.vanguard.brew.statemachine.AuditLogKata.Event.POWER_ON;
import static io.code.vanguard.brew.statemachine.AuditLogKata.State.Dirty;
import static io.code.vanguard.brew.statemachine.AuditLogKata.State.Off;

@DisplayName("StateMachine - Audit Log")
@Tag("StateMachine")
public class AuditLogTest extends BasicKataTestBase {

    private final BiPredicate<AuditLogKata.AuditCrashException, Throwable> verifyDiagnostics =
            (expected, actual) -> actual instanceof AuditLogKata.AuditCrashException actualCrash
                    && expected.getCrashedAtIndex() == actualCrash.getCrashedAtIndex()
                    && expected.getFailedEvent() == actualCrash.getFailedEvent()
                    && Objects.equals(expected.getMessage(), actualCrash.getMessage());


    @Test
    @DisplayName("Replaying a perfectly clean log results in returning to OFF.")
    @Order(1)
    void testPerfectDayLog() {
        List<Event> perfectDay = List.of(
                POWER_ON,
                BREW,
                CLEAN,
                POWER_OFF
        );

        verifyBasicKata(
                new AuditLogKata(),
                perfectDay,
                new Off(),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Replaying an incomplete log accurately restores the suspended state (DIRTY).")
    @Order(2)
    void testSuspendedStateLog() {
        List<Event> midShift = List.of(
                POWER_ON,
                BREW
        );

        verifyBasicKata(
                new AuditLogKata(),
                midShift,
                new Dirty(),
                Objects::equals
        );
    }

    @Test
    @DisplayName("A log that tries to BREW while OFF crashes at index 0.")
    @Order(3)
    void testCrashOnStartup() {
        List<Event> immediateCrash = List.of(
                BREW,
                POWER_OFF
        );

        verifyException(
                () -> new AuditLogKata().solve(immediateCrash),
                new AuditCrashException(0, BREW, "Machine malfunctioned."),
                verifyDiagnostics
        );
    }

    @Test
    @DisplayName("A log that skips the CLEAN cycle crashes at index 2.")
    @Order(4)
    void testCrashMidShift() {
        List<Event> doubleBrewCrash = List.of(
                POWER_ON,
                BREW,
                BREW,
                CLEAN
        );

        verifyException(
                () -> new AuditLogKata().solve(doubleBrewCrash),
                new AuditCrashException(2, BREW, "Machine malfunctioned."),
                verifyDiagnostics
        );
    }

    @Test
    @DisplayName("A log that tries to CLEAN while already READY crashes at index 2.")
    @Order(5)
    void testCrashOverCleaning() {
        List<Event> phantomClean = List.of(
                POWER_ON,
                BREW,
                CLEAN,
                CLEAN
        );

        verifyException(
                () -> new AuditLogKata().solve(phantomClean),
                new AuditCrashException(3, CLEAN, "Machine malfunctioned."),
                verifyDiagnostics
        );
    }
}
