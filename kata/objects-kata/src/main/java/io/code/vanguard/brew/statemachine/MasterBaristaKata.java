package io.code.vanguard.brew.statemachine;

import io.code.vanguard.brew.BasicKata;

public class MasterBaristaKata implements BasicKata<MasterBaristaKata.TransitionRequest, MasterBaristaKata.State> {

    public enum Recipe { ESPRESSO, CAPPUCCINO }

    public enum Event {
        POWER_ON,
        WARMED_UP,
        BREW_ESPRESSO,
        BREW_CAPPUCCINO,
        GRIND_DONE,
        TAMP_DONE,
        BREW_DONE,
        STEAM_DONE,
        RUN_CLEANING,
        POWER_OFF
    }

    public sealed interface State {
        record Off() implements State { }

        record Idle() implements State { }

        record Ready() implements State { }

        record Grinding(Recipe recipe) implements State { }

        record Tamping(Recipe recipe) implements State { }

        record Brewing(Recipe recipe) implements State { }

        record Steaming() implements State { }

        record NeedsCleaning() implements State { }
    }

    public record TransitionRequest(State currentState, Event event) { }

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