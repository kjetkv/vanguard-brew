package io.code.vanguard.brew.streams;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static io.code.vanguard.brew.StreamEnforcingList.list;
import static io.code.vanguard.brew.streams.HopperPipelineKata.CoffeeBean;
import static io.code.vanguard.brew.streams.HopperPipelineKata.DeliveryTruck;
import static io.code.vanguard.brew.streams.HopperPipelineKata.Pallet;
import static io.code.vanguard.brew.streams.HopperPipelineKata.Sack;

@DisplayName("Stream - Hopper Pipeline")
@Tag("Stream")
public class HopperPipelineTest extends BasicKataTestBase {


    private final CoffeeBean ethiopia = new CoffeeBean("Ethiopia");
    private final CoffeeBean colombia = new CoffeeBean("Colombia");
    private final CoffeeBean sumatra = new CoffeeBean("Sumatra");
    private final CoffeeBean brazil = new CoffeeBean("Brazil");

    @Test
    @DisplayName("Flattens a standard, fully populated delivery chain.")
    @Order(1)
    void testStandardDelivery() {
        List<DeliveryTruck> fleet = list(
                new DeliveryTruck(list(
                        new Pallet(list(
                                new Sack(list(ethiopia, colombia))
                        ))
                ))
        );

        verifyBasicKata(
                new HopperPipelineKata(),
                fleet,
                List.of(ethiopia, colombia),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Returns empty when the fleet has no trucks.")
    @Order(2)
    void testEmptyFleet() {
        verifyBasicKata(
                new HopperPipelineKata(),
                List.of(),
                List.of(),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Returns empty when trucks have no pallets.")
    @Order(3)
    void testEmptyPallets() {
        List<DeliveryTruck> fleet = list(
                new DeliveryTruck(list())
        );

        verifyBasicKata(
                new HopperPipelineKata(),
                fleet,
                List.of(),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Returns empty when pallets have no sacks.")
    @Order(4)
    void testEmptySacks() {
        List<DeliveryTruck> fleet = list(
                new DeliveryTruck(list(
                        new Pallet(list())
                ))
        );

        verifyBasicKata(
                new HopperPipelineKata(),
                fleet,
                List.of(),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Returns empty when sacks have no beans.")
    @Order(5)
    void testEmptyBeans() {
        List<DeliveryTruck> fleet = list(
                new DeliveryTruck(list(
                        new Pallet(list(
                                new Sack(list())
                        ))
                ))
        );

        verifyBasicKata(
                new HopperPipelineKata(),
                fleet,
                List.of(),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Flattens safely when multiple completely empty trucks arrive.")
    @Order(6)
    void testMultipleEmptyTrucks() {
        List<DeliveryTruck> fleet = list(
                new DeliveryTruck(list()),
                new DeliveryTruck(list())
        );

        verifyBasicKata(
                new HopperPipelineKata(),
                fleet,
                List.of(),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Extracts beans correctly when an empty truck precedes a full truck.")
    @Order(7)
    void testEmptyTruckThenFullTruck() {
        List<DeliveryTruck> fleet = list(
                new DeliveryTruck(list()),
                new DeliveryTruck(list(
                        new Pallet(list(
                                new Sack(list(ethiopia))
                        ))
                ))
        );

        verifyBasicKata(
                new HopperPipelineKata(),
                fleet,
                List.of(ethiopia),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Extracts beans correctly when a full truck precedes an empty truck.")
    @Order(8)
    void testFullTruckThenEmptyTruck() {
        List<DeliveryTruck> fleet = list(
                new DeliveryTruck(list(
                        new Pallet(list(
                                new Sack(list(ethiopia))
                        ))
                )),
                new DeliveryTruck(list())
        );

        verifyBasicKata(
                new HopperPipelineKata(),
                fleet,
                List.of(ethiopia),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Locates a single bean buried deeply within massive empty structures.")
    @Order(9)
    void testDeeplyBuriedSingleBean() {
        List<DeliveryTruck> fleet = list(
                new DeliveryTruck(list(
                        new Pallet(list(
                                new Sack(list()),
                                new Sack(list(sumatra)),
                                new Sack(list())
                        ))
                ))
        );

        verifyBasicKata(
                new HopperPipelineKata(),
                fleet,
                List.of(sumatra),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Maintains correct ordering across multiple trucks.")
    @Order(10)
    void testOrderingAcrossTrucks() {
        List<DeliveryTruck> fleet = list(
                new DeliveryTruck(list(
                        new Pallet(list(
                                new Sack(list(ethiopia))
                        ))
                )),
                new DeliveryTruck(list(
                        new Pallet(list(
                                new Sack(list(colombia))
                        ))
                ))
        );

        verifyBasicKata(
                new HopperPipelineKata(),
                fleet,
                List.of(ethiopia, colombia),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Maintains correct ordering across multiple pallets.")
    @Order(11)
    void testOrderingAcrossPallets() {
        List<DeliveryTruck> fleet = list(
                new DeliveryTruck(list(
                        new Pallet(list(
                                new Sack(list(ethiopia))
                        )),
                        new Pallet(list(
                                new Sack(list(colombia))
                        ))
                ))
        );

        verifyBasicKata(
                new HopperPipelineKata(),
                fleet,
                List.of(ethiopia, colombia),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Maintains correct ordering across multiple sacks.")
    @Order(12)
    void testOrderingAcrossSacks() {
        List<DeliveryTruck> fleet = list(
                new DeliveryTruck(list(
                        new Pallet(list(
                                new Sack(list(ethiopia)),
                                new Sack(list(colombia))
                        ))
                ))
        );

        verifyBasicKata(
                new HopperPipelineKata(),
                fleet,
                List.of(ethiopia, colombia),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Extracts all beans from a single, highly dense sack.")
    @Order(13)
    void testHighlyDenseSack() {
        List<DeliveryTruck> fleet = list(
                new DeliveryTruck(list(
                        new Pallet(list(
                                new Sack(list(ethiopia, colombia, sumatra, brazil))
                        ))
                ))
        );

        verifyBasicKata(
                new HopperPipelineKata(),
                fleet,
                List.of(ethiopia, colombia, sumatra, brazil),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Flattens correctly when empty and populated entities strictly alternate.")
    @Order(14)
    void testAlternatingEmptyAndFull() {
        List<DeliveryTruck> fleet = list(
                new DeliveryTruck(list(
                        new Pallet(list()),
                        new Pallet(list(
                                new Sack(list(ethiopia))
                        ))
                )),
                new DeliveryTruck(list(
                        new Pallet(list(
                                new Sack(list())
                        )),
                        new Pallet(list(
                                new Sack(list(colombia))
                        ))
                ))
        );

        verifyBasicKata(
                new HopperPipelineKata(),
                fleet,
                List.of(ethiopia, colombia),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Successfully processes a completely scattered distribution of beans.")
    @Order(15)
    void testScatteredDistribution() {
        List<DeliveryTruck> fleet = list(
                new DeliveryTruck(list(
                        new Pallet(list(
                                new Sack(list(ethiopia)),
                                new Sack(list())
                        ))
                )),
                new DeliveryTruck(list()),
                new DeliveryTruck(list(
                        new Pallet(list(
                                new Sack(list()),
                                new Sack(list(colombia, sumatra))
                        ))
                ))
        );

        verifyBasicKata(
                new HopperPipelineKata(),
                fleet,
                List.of(ethiopia, colombia, sumatra),
                Objects::equals
        );
    }
}