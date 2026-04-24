package io.code.vanguard.brew.statemachine;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Validators.verifySameExceptionClassAndMessage;
import static io.code.vanguard.brew.statemachine.CoffeeVendingMachineKata.CoffeeType.CAPPUCCINO;
import static io.code.vanguard.brew.statemachine.CoffeeVendingMachineKata.CoffeeType.ESPRESSO;
import static io.code.vanguard.brew.statemachine.CoffeeVendingMachineKata.CoffeeType.LATTE;
import static io.code.vanguard.brew.statemachine.CoffeeVendingMachineKata.Event.Cancel;
import static io.code.vanguard.brew.statemachine.CoffeeVendingMachineKata.Event.DispenseComplete;
import static io.code.vanguard.brew.statemachine.CoffeeVendingMachineKata.Event.InsertCoin;
import static io.code.vanguard.brew.statemachine.CoffeeVendingMachineKata.Event.SelectCoffee;
import static io.code.vanguard.brew.statemachine.CoffeeVendingMachineKata.Event.TakeChange;
import static io.code.vanguard.brew.statemachine.CoffeeVendingMachineKata.InvalidTransitionException;
import static io.code.vanguard.brew.statemachine.CoffeeVendingMachineKata.State.AcceptingMoney;
import static io.code.vanguard.brew.statemachine.CoffeeVendingMachineKata.State.ChangeDue;
import static io.code.vanguard.brew.statemachine.CoffeeVendingMachineKata.State.Dispensing;
import static io.code.vanguard.brew.statemachine.CoffeeVendingMachineKata.State.Idle;
import static io.code.vanguard.brew.statemachine.CoffeeVendingMachineKata.State.SelectionMade;
import static io.code.vanguard.brew.statemachine.CoffeeVendingMachineKata.TransitionRequest;

@DisplayName("StateMachine - Coffee Vending Machine")
@Tag("StateMachine")
public class CoffeeVendingMachineTest extends BasicKataTestBase {

    @Test
    @DisplayName("When IDLE, selecting a coffee moves to SELECTION_MADE.")
    @Order(1)
    void testSelectCoffee() {
        verifyBasicKata(
                new CoffeeVendingMachineKata(),
                new TransitionRequest(new Idle(), new SelectCoffee(LATTE)),
                new SelectionMade(LATTE),
                Objects::equals
        );
    }

    @Test
    @DisplayName("When SELECTION_MADE, inserting a coin moves to ACCEPTING_MONEY.")
    @Order(2)
    void testInsertFirstCoin() {
        verifyBasicKata(
                new CoffeeVendingMachineKata(),
                new TransitionRequest(new SelectionMade(ESPRESSO), new InsertCoin(100)),
                new AcceptingMoney(ESPRESSO, 100),
                Objects::equals
        );
    }

    @Test
    @DisplayName("When ACCEPTING_MONEY, inserting another coin updates the amount.")
    @Order(3)
    void testInsertMoreCoins() {
        verifyBasicKata(
                new CoffeeVendingMachineKata(),
                new TransitionRequest(new AcceptingMoney(CAPPUCCINO, 150), new InsertCoin(50)),
                new AcceptingMoney(CAPPUCCINO, 200),
                Objects::equals
        );
    }

    @Test
    @DisplayName("When enough money is inserted, transitions to DISPENSING.")
    @Order(4)
    void testSufficientFunds() {
        verifyBasicKata(
                new CoffeeVendingMachineKata(),
                new TransitionRequest(new AcceptingMoney(ESPRESSO, 200), new InsertCoin(50)),
                new Dispensing(ESPRESSO, 250),
                Objects::equals
        );
    }

    @Test
    @DisplayName("When overpaid, transitions to CHANGE_DUE after dispensing.")
    @Order(5)
    void testDispenseWithChange() {
        verifyBasicKata(
                new CoffeeVendingMachineKata(),
                new TransitionRequest(new Dispensing(LATTE, 400), new DispenseComplete()),
                new ChangeDue(50),
                Objects::equals
        );
    }

    @Test
    @DisplayName("When CHANGE_DUE, taking change returns to IDLE.")
    @Order(6)
    void testTakeChange() {
        verifyBasicKata(
                new CoffeeVendingMachineKata(),
                new TransitionRequest(new ChangeDue(50), new TakeChange()),
                new Idle(),
                Objects::equals
        );
    }

    @Test
    @DisplayName("When SELECTION_MADE, can cancel to return to IDLE.")
    @Order(7)
    void testCancelAtSelection() {
        verifyBasicKata(
                new CoffeeVendingMachineKata(),
                new TransitionRequest(new SelectionMade(ESPRESSO), new Cancel()),
                new Idle(),
                Objects::equals
        );
    }

    @Test
    @DisplayName("When ACCEPTING_MONEY, can cancel to get change for money inserted.")
    @Order(8)
    void testCancelWhileAcceptingMoney() {
        verifyBasicKata(
                new CoffeeVendingMachineKata(),
                new TransitionRequest(new AcceptingMoney(LATTE, 150), new Cancel()),
                new ChangeDue(150),
                Objects::equals
        );
    }

    @Test
    @DisplayName("When IDLE, cannot insert a coin before selection.")
    @Order(9)
    void testInsertCoinBeforeSelection() {
        verifyException(
                () -> new CoffeeVendingMachineKata().solve(new TransitionRequest(
                        new Idle(),
                        new InsertCoin(100))),
                new InvalidTransitionException("Please select a coffee first."),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("When ACCEPTING_MONEY, cannot select another coffee.")
    @Order(10)
    void testSelectCoffeeWhileAcceptingMoney() {
        verifyException(
                () -> new CoffeeVendingMachineKata().solve(new TransitionRequest(
                        new AcceptingMoney(ESPRESSO, 100),
                        new SelectCoffee(LATTE))),
                new InvalidTransitionException("Cannot select a new coffee while payment is in progress."),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("When ACCEPTING_MONEY, cannot take change.")
    @Order(11)
    void testTakeChangeWhileAcceptingMoney() {
        verifyException(
                () -> new CoffeeVendingMachineKata().solve(new TransitionRequest(
                        new AcceptingMoney(CAPPUCCINO, 300),
                        new TakeChange())),
                new InvalidTransitionException("Cannot take change while payment is in progress."),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("When IDLE, cannot take change.")
    @Order(12)
    void testTakeChangeWhenIdle() {
        verifyException(
                () -> new CoffeeVendingMachineKata().solve(new TransitionRequest(new Idle(), new TakeChange())),
                new InvalidTransitionException("No change is due."),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("When DISPENSING, cannot insert a coin.")
    @Order(13)
    void testInsertCoinWhileDispensing() {
        verifyException(
                () -> new CoffeeVendingMachineKata().solve(new TransitionRequest(
                        new Dispensing(LATTE, 400),
                        new InsertCoin(50))),
                new InvalidTransitionException("Cannot insert coin while dispensing."),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("When DISPENSING, cannot cancel.")
    @Order(14)
    void testCancelWhileDispensing() {
        verifyException(
                () -> new CoffeeVendingMachineKata().solve(new TransitionRequest(
                        new Dispensing(ESPRESSO, 300),
                        new Cancel())),
                new InvalidTransitionException("Cannot cancel while dispensing."),
                verifySameExceptionClassAndMessage
        );
    }
}



