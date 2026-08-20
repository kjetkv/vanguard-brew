package io.code.vanguard.brew.functional;

import io.code.vanguard.brew.BasicKata;

import java.util.Optional;

import static io.code.vanguard.brew.functional.SpillProofFunnelKata.CoffeeOrder;
import static io.code.vanguard.brew.functional.SpillProofFunnelKata.OrderManifest;

public class SpillProofFunnelKata implements BasicKata<CoffeeOrder, OrderManifest> {

    public interface Address {
        Optional<String> street();

        Optional<String> city();

        Optional<String> zipCode();
    }

    public interface Email {
        Optional<String> address();

        boolean isVerified();
    }

    public interface Customer {
        Optional<String> name();

        boolean isVip();

        Optional<Address> address();

        Optional<Email> email();
    }

    public interface CoffeeOrder {
        Optional<String> orderId();

        Optional<Customer> customer();
    }

    public record OrderManifest(String zipCode, String routingName, String contactEmail) { }

    @Override
    public OrderManifest solve(CoffeeOrder coffeeOrder) {
        return null;
    }
}