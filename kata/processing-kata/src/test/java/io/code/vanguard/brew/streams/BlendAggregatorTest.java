package io.code.vanguard.brew.streams;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.code.vanguard.brew.StreamEnforcingList.list;
import static io.code.vanguard.brew.streams.BlendAggregatorKata.CoffeeBean;

@DisplayName("Stream - Blend Aggregator")
@Tag("Stream")
public class BlendAggregatorTest extends BasicKataTestBase {

    @Test
    @DisplayName("Aggregates a standard mixed batch accurately.")
    @Order(1)
    void testStandardMixedBatch() {
        verifyBasicKata(
                new BlendAggregatorKata(),
                list(
                        new CoffeeBean("Colombia", false, 500),
                        new CoffeeBean("Colombia", true, 200),
                        new CoffeeBean("Ethiopia", false, 300),
                        new CoffeeBean("Ethiopia", false, 100),
                        new CoffeeBean("Sumatra", true, 400)
                ),
                Map.of(
                        true, Map.of("Colombia", 200, "Sumatra", 400),
                        false, Map.of("Colombia", 500, "Ethiopia", 400)
                ),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Returns empty inner maps when the conveyor belt is completely empty.")
    @Order(2)
    void testEmptyBatch() {
        verifyBasicKata(
                new BlendAggregatorKata(),
                List.of(),
                Map.of(
                        true, Map.of(),
                        false, Map.of()
                ),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Aggregates a batch containing strictly caffeinated beans.")
    @Order(3)
    void testAllCaffeinated() {
        verifyBasicKata(
                new BlendAggregatorKata(),
                list(
                        new CoffeeBean("Brazil", false, 1000),
                        new CoffeeBean("Brazil", false, 500)
                ),
                Map.of(
                        true, Map.of(),
                        false, Map.of("Brazil", 1500)
                ),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Aggregates a batch containing strictly decaf beans.")
    @Order(4)
    void testAllDecaf() {
        verifyBasicKata(
                new BlendAggregatorKata(),
                list(
                        new CoffeeBean("Peru", true, 800),
                        new CoffeeBean("Honduras", true, 200)
                ),
                Map.of(
                        true, Map.of("Peru", 800, "Honduras", 200),
                        false, Map.of()
                ),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Processes a single bean correctly.")
    @Order(5)
    void testSingleBean() {
        verifyBasicKata(
                new BlendAggregatorKata(),
                list(
                        new CoffeeBean("Kenya", false, 250)
                ),
                Map.of(
                        true, Map.of(),
                        false, Map.of("Kenya", 250)
                ),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Sums massive quantities of the exact same bean profile.")
    @Order(6)
    void testSameOriginSameTypeMultipleTimes() {
        verifyBasicKata(
                new BlendAggregatorKata(),
                list(
                        new CoffeeBean("Vietnam", false, 100),
                        new CoffeeBean("Vietnam", false, 100),
                        new CoffeeBean("Vietnam", false, 100),
                        new CoffeeBean("Vietnam", false, 100)
                ),
                Map.of(
                        true, Map.of(),
                        false, Map.of("Vietnam", 400)
                ),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Partitions correctly when the origin is identical but decaf status differs.")
    @Order(7)
    void testSameOriginMixedDecafStatus() {
        verifyBasicKata(
                new BlendAggregatorKata(),
                list(
                        new CoffeeBean("Colombia", true, 50),
                        new CoffeeBean("Colombia", false, 150),
                        new CoffeeBean("Colombia", true, 50)
                ),
                Map.of(
                        true, Map.of("Colombia", 100),
                        false, Map.of("Colombia", 150)
                ),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Handles beans with exactly zero weight.")
    @Order(8)
    void testZeroWeightBeans() {
        verifyBasicKata(
                new BlendAggregatorKata(),
                list(
                        new CoffeeBean("Ghost", false, 0),
                        new CoffeeBean("Ghost", true, 0)
                ),
                Map.of(
                        true, Map.of("Ghost", 0),
                        false, Map.of("Ghost", 0)
                ),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Completely filters out beans with negative weights before aggregation.")
    @Order(9)
    void testNegativeWeightBeansFilteredOut() {
        verifyBasicKata(
                new BlendAggregatorKata(),
                list(
                        new CoffeeBean("Ethiopia", false, 1000),
                        new CoffeeBean("Ethiopia", false, -200),
                        new CoffeeBean("Sumatra", true, -500)
                ),
                Map.of(
                        true, Map.of(),
                        false, Map.of("Ethiopia", 1000)
                ),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Groups beans accurately even when the origin string is completely empty.")
    @Order(10)
    void testEmptyOriginString() {
        verifyBasicKata(
                new BlendAggregatorKata(),
                list(
                        new CoffeeBean("", false, 500),
                        new CoffeeBean("", false, 300)
                ),
                Map.of(
                        true, Map.of(),
                        false, Map.of("", 800)
                ),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Separates differently cased origins as distinct groups.")
    @Order(11)
    void testCaseSensitivityInOrigins() {
        verifyBasicKata(
                new BlendAggregatorKata(),
                list(
                        new CoffeeBean("colombia", false, 100),
                        new CoffeeBean("Colombia", false, 200)
                ),
                Map.of(
                        true, Map.of(),
                        false, Map.of("colombia", 100, "Colombia", 200)
                ),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Aggregates successfully across a highly diverse set of distinct origins.")
    @Order(12)
    void testHighlyDiverseOrigins() {
        verifyBasicKata(
                new BlendAggregatorKata(),
                list(
                        new CoffeeBean("Guatemala", false, 10),
                        new CoffeeBean("Costa Rica", false, 20),
                        new CoffeeBean("Rwanda", false, 30),
                        new CoffeeBean("Panama", false, 40),
                        new CoffeeBean("Burundi", false, 50)
                ),
                Map.of(
                        true, Map.of(),
                        false, Map.of("Guatemala", 10, "Costa Rica", 20, "Rwanda", 30, "Panama", 40, "Burundi", 50)
                ),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Maintains stability with a completely alternating sequence of constraints.")
    @Order(13)
    void testAlternatingConstraints() {
        verifyBasicKata(
                new BlendAggregatorKata(),
                list(
                        new CoffeeBean("Mexico", true, 1),
                        new CoffeeBean("Kenya", false, 2),
                        new CoffeeBean("Mexico", true, 3),
                        new CoffeeBean("Kenya", false, 4)
                ),
                Map.of(
                        true, Map.of("Mexico", 4),
                        false, Map.of("Kenya", 6)
                ),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Handles aggregation correctly when a large number of zero-weight beans are processed.")
    @Order(14)
    void testMassiveZeroWeightAggregation() {
        verifyBasicKata(
                new BlendAggregatorKata(),
                list(
                        new CoffeeBean("Brazil", true, 0),
                        new CoffeeBean("Brazil", true, 0),
                        new CoffeeBean("Brazil", true, 0)
                ),
                Map.of(
                        true, Map.of("Brazil", 0),
                        false, Map.of()
                ),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Processes a massive weight sum just below Integer bounds.")
    @Order(15)
    void testLargeWeightSum() {
        verifyBasicKata(
                new BlendAggregatorKata(),
                list(
                        new CoffeeBean("Sumatra", false, 1000000000),
                        new CoffeeBean("Sumatra", false, 1000000000)
                ),
                Map.of(
                        true, Map.of(),
                        false, Map.of("Sumatra", 2000000000)
                ),
                Objects::equals
        );
    }
}