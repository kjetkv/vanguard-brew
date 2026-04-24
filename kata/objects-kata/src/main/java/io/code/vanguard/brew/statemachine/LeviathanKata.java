package io.code.vanguard.brew.statemachine;

import io.code.vanguard.brew.BasicKata;

import static io.code.vanguard.brew.statemachine.LeviathanKata.State;
import static io.code.vanguard.brew.statemachine.LeviathanKata.TransitionRequest;

public class LeviathanKata implements BasicKata<TransitionRequest, State> {

    public enum Event {
        POWER_ON,
        POWER_OFF,
        E_STOP,
        BREW_CMD,
        GRIND_DONE,
        BREW_DONE,
        STEAM_CMD,
        STEAM_DONE
    }

    public record Inventory(int beansGrams, int waterMl) {
        public boolean hasEnoughForBrew() {
            return beansGrams >= 18 && waterMl >= 50;
        }
    }

    public sealed interface BrewState {
        record BrewerIdle() implements BrewState { }

        record Grinding() implements BrewState { }

        record Brewing() implements BrewState { }
    }

    public sealed interface SteamState {
        record SteamerIdle() implements SteamState { }

        record Steaming() implements SteamState { }
    }

    public sealed interface State {
        record Off() implements State { }

        record Active(BrewState brewState, SteamState steamState) implements State { }
    }

    public record TransitionRequest(State currentState, Event event, Inventory inventory) { }

    public static class InvalidTransitionException extends IllegalStateException {
        public InvalidTransitionException(String msg) {
            super(msg);
        }
    }

    public static class InsufficientResourcesException extends IllegalStateException {
        public InsufficientResourcesException(String msg) {
            super(msg);
        }
    }

    @Override
    public State solve(TransitionRequest request) {
        return null;
    }
}