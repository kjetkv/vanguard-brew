package io.code.vanguard.brew.statemachine;

import io.code.vanguard.brew.BasicKata;

import static io.code.vanguard.brew.statemachine.SimpleCoffeeMachineKata.State;
import static io.code.vanguard.brew.statemachine.SimpleCoffeeMachineKata.TransitionRequest;

public class SimpleCoffeeMachineKata implements BasicKata<TransitionRequest, State> {

    public enum State { OFF, IDLE, READY, BREWING }

    public enum Event { POWER_ON, WARM_UP, MAKE_COFFEE, POWER_OFF }

    public record TransitionRequest(State currentState, Event event) {
    }

    public static class InvalidTransitionException extends IllegalStateException {
        public InvalidTransitionException(String message) {
            super(message);
        }
    }

    @Override
    public State solve(TransitionRequest request) {
        return null;
    }
}
