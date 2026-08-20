package io.code.vanguard.brew.functional;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Optional;

import static io.code.vanguard.brew.functional.SpillProofFunnelKata.Address;
import static io.code.vanguard.brew.functional.SpillProofFunnelKata.CoffeeOrder;
import static io.code.vanguard.brew.functional.SpillProofFunnelKata.Customer;
import static io.code.vanguard.brew.functional.SpillProofFunnelKata.Email;
import static io.code.vanguard.brew.functional.SpillProofFunnelKata.OrderManifest;
import static java.util.Optional.empty;
import static java.util.Optional.of;

@DisplayName("Functional - Spill-Proof Funnel")
@Tag("Functional")
public class SpillProofFunnelTest extends BasicKataTestBase {

    private record MockAddress(Optional<String> street,
                               Optional<String> city,
                               Optional<String> zipCode) implements Address { }

    private record MockEmail(Optional<String> address, boolean isVerified) implements Email { }

    private record MockCustomer(Optional<String> name,
                                boolean isVip,
                                Optional<Address> address,
                                Optional<Email> email) implements Customer { }

    private record MockCoffeeOrder(Optional<String> orderId, Optional<Customer> customer) implements CoffeeOrder { }


    @Test
    @DisplayName("Successfully processes a perfect, fully populated VIP order.")
    @Order(1)
    void testPerfectOrder() {
        MockEmail email = new MockEmail(of("alice@vanguardbrew.com"), true);
        MockAddress address = new MockAddress(of("123 Roaster Way"), of("Melbourne"), of("0101"));
        MockCustomer customer = new MockCustomer(of("Alice"), true, of(address), of(email));
        MockCoffeeOrder order = new MockCoffeeOrder(of("ORD-001"), of(customer));

        OrderManifest expected = new OrderManifest(
                "0101",
                "Alice",
                "alice@vanguardbrew.com"
        );

        verifyBasicKata(
                new SpillProofFunnelKata(),
                order,
                expected,
                Objects::equals);
    }

    @Test
    @DisplayName("Gracefully handles a completely null root Order payload.")
    @Order(2)
    void testCompletelyNullOrder() {
        OrderManifest expected = new OrderManifest(
                "UNKNOWN_ZIP",
                "GUEST",
                "NO_VERIFIED_EMAIL"
        );

        verifyBasicKata(
                new SpillProofFunnelKata(),
                null,
                expected,
                Objects::equals);
    }

    @Test
    @DisplayName("Gracefully handles a missing Customer object (Optional.empty).")
    @Order(3)
    void testMissingCustomer() {
        MockCoffeeOrder order = new MockCoffeeOrder(of("ORD-002"), empty());

        OrderManifest expected = new OrderManifest(
                "UNKNOWN_ZIP",
                "GUEST",
                "NO_VERIFIED_EMAIL"
        );

        verifyBasicKata(
                new SpillProofFunnelKata(),
                order,
                expected,
                Objects::equals);
    }

    @Test
    @DisplayName("Gracefully handles a missing Address object (Optional.empty).")
    @Order(4)
    void testMissingAddress() {
        MockEmail email = new MockEmail(of("bob@vanguardbrew.com"), true);
        MockCustomer customer = new MockCustomer(of("Bob"), true, empty(), of(email));
        MockCoffeeOrder order = new MockCoffeeOrder(of("ORD-003"), of(customer));

        OrderManifest expected = new OrderManifest(
                "UNKNOWN_ZIP",
                "Bob",
                "bob@vanguardbrew.com"
        );

        verifyBasicKata(
                new SpillProofFunnelKata(),
                order,
                expected,
                Objects::equals);
    }

    @Test
    @DisplayName("Gracefully handles a missing Zip Code string inside a valid Address.")
    @Order(5)
    void testMissingZipCode() {
        MockEmail email = new MockEmail(of("charlie@vanguardbrew.com"), true);
        MockAddress address = new MockAddress(of("404 Null St"), of("Melbourne"), empty());
        MockCustomer customer = new MockCustomer(of("Charlie"), true, of(address), of(email));
        MockCoffeeOrder order = new MockCoffeeOrder(of("ORD-004"), of(customer));

        OrderManifest expected = new OrderManifest(
                "UNKNOWN_ZIP",
                "Charlie",
                "charlie@vanguardbrew.com"
        );

        verifyBasicKata(
                new SpillProofFunnelKata(),
                order,
                expected,
                Objects::equals);
    }

    @Test
    @DisplayName("Defaults to GUEST routing name when the Customer is not a VIP.")
    @Order(6)
    void testNonVipCustomer() {
        MockEmail email = new MockEmail(of("dave@vanguardbrew.com"), true);
        MockAddress address = new MockAddress(of("55 Standard Ave"), of("Melbourne"), of("0202"));

        MockCustomer customer = new MockCustomer(of("Dave"), false, of(address), of(email));
        MockCoffeeOrder order = new MockCoffeeOrder(of("ORD-005"), of(customer));

        OrderManifest expected = new OrderManifest(
                "0202",
                "GUEST",
                "dave@vanguardbrew.com"
        );

        verifyBasicKata(
                new SpillProofFunnelKata(),
                order,
                expected,
                Objects::equals);
    }

    @Test
    @DisplayName("Rejects an email that exists but is not verified.")
    @Order(7)
    void testUnverifiedEmail() {

        MockEmail email = new MockEmail(of("eve@vanguardbrew.com"), false);
        MockAddress address = new MockAddress(of("123 Roaster Way"), of("Melbourne"), of("0101"));
        MockCustomer customer = new MockCustomer(of("Eve"), true, of(address), of(email));
        MockCoffeeOrder order = new MockCoffeeOrder(of("ORD-006"), of(customer));

        OrderManifest expected = new OrderManifest(
                "0101",
                "Eve",
                "NO_VERIFIED_EMAIL"
        );

        verifyBasicKata(
                new SpillProofFunnelKata(),
                order,
                expected,
                Objects::equals);
    }

    @Test
    @DisplayName("Gracefully handles a completely missing email object (Optional.empty).")
    @Order(8)
    void testMissingEmailObject() {
        MockAddress address = new MockAddress(of("123 Roaster Way"), of("Melbourne"), of("0101"));
        MockCustomer customer = new MockCustomer(of("Frank"), true, of(address), empty());
        MockCoffeeOrder order = new MockCoffeeOrder(of("ORD-007"), of(customer));

        OrderManifest expected = new OrderManifest(
                "0101",
                "Frank",
                "NO_VERIFIED_EMAIL"
        );

        verifyBasicKata(
                new SpillProofFunnelKata(),
                order,
                expected,
                Objects::equals);
    }
}