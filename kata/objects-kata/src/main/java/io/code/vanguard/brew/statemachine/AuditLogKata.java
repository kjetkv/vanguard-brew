package io.code.vanguard.brew.statemachine;

import io.code.vanguard.brew.BasicKata;

import java.util.List;

public class AuditLogKata implements BasicKata<List<AuditLogKata.Event>, AuditLogKata.State> {

    public enum Event { POWER_ON, BREW, CLEAN, POWER_OFF }

    public sealed interface State {
        record Off() implements State { }

        record Ready() implements State { }

        record Dirty() implements State { }
    }

    private record TransitionRequest(State currentState, Event event) { }

    public static class AuditCrashException extends RuntimeException {
        private final int crashedAtIndex;
        private final Event failedEvent;

        public AuditCrashException(int crashedAtIndex, Event failedEvent, String message) {
            super(message);
            this.crashedAtIndex = crashedAtIndex;
            this.failedEvent = failedEvent;
        }

        public int getCrashedAtIndex() {
            return crashedAtIndex;
        }

        public Event getFailedEvent() {
            return failedEvent;
        }
    }

    private State applyTransition(State current, Event event) {
        return switch (new TransitionRequest(current, event)) {
            case TransitionRequest(State.Off __, var e) when e == Event.POWER_ON -> new State.Ready();
            case TransitionRequest(State.Ready __, var e) when e == Event.BREW -> new State.Dirty();
            case TransitionRequest(State.Ready __, var e) when e == Event.POWER_OFF -> new State.Off();
            case TransitionRequest(State.Dirty __, var e) when e == Event.CLEAN -> new State.Ready();
            case TransitionRequest(State.Dirty __, var e) when e == Event.POWER_OFF -> new State.Off();
            default -> throw new IllegalStateException("Machine malfunctioned.");
        };
    }

    @Override
    public State solve(List<Event> auditLog) {
        return null;
    }
}
