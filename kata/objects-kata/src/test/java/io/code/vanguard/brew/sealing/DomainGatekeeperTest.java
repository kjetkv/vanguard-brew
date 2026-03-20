package io.code.vanguard.brew.sealing;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.sealing.DomainGatekeeperKata.Admin;
import static io.code.vanguard.brew.sealing.DomainGatekeeperKata.Guest;
import static io.code.vanguard.brew.sealing.DomainGatekeeperKata.Member;

@DisplayName("Sealing - Domain Gatekeeper")
@Tag("Sealing")
public class DomainGatekeeperTest extends BasicKataTestBase {

    @Test
    @DisplayName("When providing a Guest account, returns the Limited access string.")
    @Order(1)
    void testGuestAccess() {
        verifyBasicKata(
                new DomainGatekeeperKata(),
                new Guest("session_8921"),
                "Access Level: Limited (Guest)",
                Objects::equals
        );
    }

    @Test
    @DisplayName("When providing a Member account, extracts the tier and returns the Standard access string.")
    @Order(2)
    void testMemberAccess() {
        verifyBasicKata(
                new DomainGatekeeperKata(),
                new Member("alice_dev", "Gold"),
                "Access Level: Standard (Gold)",
                Objects::equals
        );
    }

    @Test
    @DisplayName("When providing an Admin account, extracts the clearance and returns the Maximum access string.")
    @Order(3)
    void testAdminAccess() {
        verifyBasicKata(
                new DomainGatekeeperKata(),
                new Admin("super_bob", 5),
                "Access Level: Maximum (Clearance 5)",
                Objects::equals
        );
    }

    @Test
    @DisplayName("When providing a null account, natively throws a NullPointerException.")
    @Order(4)
    void testNullAccountThrowsNPE() {
        verifyException(
                () -> new DomainGatekeeperKata().solve(null),
                new NullPointerException()
        );
    }
}
