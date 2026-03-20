package io.code.vanguard.brew.sealing;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Extractors.parentInterfaceForClass;
import static io.code.vanguard.brew.sealing.GuardedVaultKata.Deposit;
import static io.code.vanguard.brew.sealing.GuardedVaultKata.Transaction;
import static io.code.vanguard.brew.sealing.GuardedVaultKata.Withdrawal;

@DisplayName("Sealing - Guarded Vault")
@Tag("Sealing")
public class GuardedVaultTest extends BasicKataTestBase {

    @Test
    @DisplayName("Transaction must be a sealed interface.")
    @Order(1)
    void testTransactionIsSealed() {
        verifyClass(
                Transaction.class,
                Class::isSealed,
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Deposit must be a Record.")
    @Order(2)
    void testDepositIsRecord() {
        verifyClass(
                Deposit.class,
                Class::isRecord,
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Deposit must implement Transaction.")
    @Order(3)
    void testDepositInheritance() {
        verifyClass(
                Deposit.class,
                parentInterfaceForClass, // Look how clean this is now!
                "Parent-interface: Transaction",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Withdrawal must be a Record.")
    @Order(4)
    void testWithdrawalIsRecord() {
        verifyClass(
                Withdrawal.class,
                Class::isRecord,
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Withdrawal must implement Transaction.")
    @Order(5)
    void testWithdrawalInheritance() {
        verifyClass(
                Withdrawal.class,
                parentInterfaceForClass,
                "Parent-interface: Transaction",
                Objects::equals
        );
    }

    @Test
    @DisplayName("When depositing, correctly matches the Deposit record.")
    @Order(6)
    void testDeposit() {
        verifyBasicKata(
                new GuardedVaultKata(),
                new Deposit(500.0),
                "Deposited: $500.0",
                Objects::equals
        );
    }

    @Test
    @DisplayName("When withdrawing a standard amount, matches the standard Withdrawal branch.")
    @Order(7)
    void testNormalWithdrawal() {
        verifyBasicKata(
                new GuardedVaultKata(),
                new Withdrawal(1000.0),
                "Withdrawn: $1000.0",
                Objects::equals
        );
    }

    @Test
    @DisplayName("When withdrawing over 10,000, flags the transaction.")
    @Order(8)
    void testFlaggedWithdrawal() {
        verifyBasicKata(
                new GuardedVaultKata(),
                new Withdrawal(15000.0),
                "Flagged for Review: $15000.0",
                Objects::equals
        );
    }

    @Test
    @DisplayName("When providing a null transaction, natively throws NPE.")
    @Order(9)
    void testNullTransactionThrowsNPE() {
        verifyException(
                () -> new GuardedVaultKata().solve(null),
                new NullPointerException()
        );
    }
}