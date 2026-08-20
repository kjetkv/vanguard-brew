package io.code.vanguard.brew.functional;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.functional.QualityGateKata.Bean;
import static io.code.vanguard.brew.functional.QualityGateKata.RuleType;

@DisplayName("Functional - Quality Gate")
@Tag("Functional")
public class QualityGateTest extends BasicKataTestBase {

    @Test
    @DisplayName("Accepts beans from Ethiopia.")
    @Order(1)
    void testOriginAcceptsEthiopia() {
        Bean testBean = new Bean("Ethiopia", 1, true);

        verifyFunctionalKata(
                new QualityGateKata(),
                RuleType.PREMIUM_ORIGIN,
                true,
                predicate -> predicate.test(testBean),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Accepts beans from Colombia.")
    @Order(2)
    void testOriginAcceptsColombia() {
        Bean testBean = new Bean("Colombia", 1, true);

        verifyFunctionalKata(
                new QualityGateKata(),
                RuleType.PREMIUM_ORIGIN,
                true,
                predicate -> predicate.test(testBean),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Rejects beans from Mars.")
    @Order(3)
    void testOriginRejectsMars() {
        Bean testBean = new Bean("Mars", 1, true);

        verifyFunctionalKata(
                new QualityGateKata(),
                RuleType.PREMIUM_ORIGIN,
                false,
                predicate -> predicate.test(testBean),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Accepts roast level 3.")
    @Order(4)
    void testRoastAcceptsLevel3() {
        Bean testBean = new Bean("Any", 3, true);

        verifyFunctionalKata(
                new QualityGateKata(),
                RuleType.DARK_ROAST,
                true,
                predicate -> predicate.test(testBean),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Rejects roast level 2.")
    @Order(5)
    void testRoastRejectsLevel2() {
        Bean testBean = new Bean("Any", 2, true);

        verifyFunctionalKata(
                new QualityGateKata(),
                RuleType.DARK_ROAST,
                false,
                predicate -> predicate.test(testBean),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Accepts clean beans.")
    @Order(6)
    void testDefectAcceptsClean() {
        Bean testBean = new Bean("Any", 1, false);

        verifyFunctionalKata(
                new QualityGateKata(),
                RuleType.NOT_DEFECTIVE,
                true,
                predicate -> predicate.test(testBean),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Rejects defective beans.")
    @Order(7)
    void testDefectRejectsDefective() {
        Bean testBean = new Bean("Any", 1, true);

        verifyFunctionalKata(
                new QualityGateKata(),
                RuleType.NOT_DEFECTIVE,
                false,
                predicate -> predicate.test(testBean),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Accepts the perfect bean.")
    @Order(8)
    void testMasterGateAcceptsPerfectBean() {
        Bean perfectBean = new Bean("Ethiopia", 4, false);

        verifyFunctionalKata(
                new QualityGateKata(),
                RuleType.QUALITY_GATE,
                true,
                predicate -> predicate.test(perfectBean),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Rejects due to bad origin.")
    @Order(9)
    void testMasterGateRejectsOrigin() {
        Bean flawedBean = new Bean("Mars", 4, false);

        verifyFunctionalKata(
                new QualityGateKata(),
                RuleType.QUALITY_GATE,
                false,
                predicate -> predicate.test(flawedBean),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Rejects due to light roast.")
    @Order(10)
    void testMasterGateRejectsRoast() {
        Bean flawedBean = new Bean("Ethiopia", 2, false);

        verifyFunctionalKata(
                new QualityGateKata(),
                RuleType.QUALITY_GATE,
                false,
                predicate -> predicate.test(flawedBean),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Rejects due to defect.")
    @Order(11)
    void testMasterGateRejectsDefect() {
        Bean flawedBean = new Bean("Ethiopia", 4, true);

        verifyFunctionalKata(
                new QualityGateKata(),
                RuleType.QUALITY_GATE,
                false,
                predicate -> predicate.test(flawedBean),
                Objects::equals
        );
    }
}