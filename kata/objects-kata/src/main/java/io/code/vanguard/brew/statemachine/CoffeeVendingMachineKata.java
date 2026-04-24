package io.code.vanguard.brew.statemachine;

import io.code.vanguard.brew.BasicKata;

import static io.code.vanguard.brew.statemachine.CoffeeVendingMachineKata.State;
import static io.code.vanguard.brew.statemachine.CoffeeVendingMachineKata.TransitionRequest;

public class CoffeeVendingMachineKata implements BasicKata<TransitionRequest, State> {

    public enum CoffeeType {
        ESPRESSO(250),
        LATTE(350),
        CAPPUCCINO(400);

        private final int price;

        CoffeeType(int price) {
            this.price = price;
        }

        public int getPrice() {
            return price;
        }
    }

    public sealed interface Event {
        record SelectCoffee(CoffeeType coffeeType) implements Event { }

        record InsertCoin(int amount) implements Event { }

        record DispenseComplete() implements Event { }

        record TakeChange() implements Event { }

        record Cancel() implements Event { }
    }

    public sealed interface State {
        record Idle() implements State { }

        record SelectionMade(CoffeeType coffeeType) implements State { }

        record AcceptingMoney(CoffeeType coffeeType, int amountInserted) implements State { }

        record Dispensing(CoffeeType coffeeType, int amountInserted) implements State { }

        record ChangeDue(int changeAmount) implements State { }
    }

    public record TransitionRequest(State currentState, Event event) { }

    public static class InvalidTransitionException extends RuntimeException {
        public InvalidTransitionException(String message) {
            super(message);
        }
    }

    @Override
    public State solve(TransitionRequest request) {
        return null;
    }
}
