package io.code.vanguard.brew.streams;

import io.code.vanguard.brew.BasicKataTestBase;
import io.code.vanguard.brew.StreamEnforcingList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

import static io.code.vanguard.brew.streams.BeanSorterKata.Cherry;
import static io.code.vanguard.brew.streams.BeanSorterKata.CoffeeBean;

@DisplayName("Stream - Bean Sorter")
@Tag("Stream")
public class BeanSorterTest extends BasicKataTestBase {

    private final CoffeeBean ethiopia = new CoffeeBean("Ethiopia", 95);
    private final CoffeeBean colombia = new CoffeeBean("Colombia", 85);
    private final CoffeeBean brazil = new CoffeeBean("Brazil", 85);
    private final CoffeeBean sumatra = new CoffeeBean("Sumatra", 70);
    private final CoffeeBean robusta = new CoffeeBean("Robusta", 0);
    private final CoffeeBean mutant = new CoffeeBean("Mutant", -50);
    private final CoffeeBean alien = new CoffeeBean("Alien", Integer.MAX_VALUE);

    private final CoffeeBean buggy = new CoffeeBean("Buggy", 10);
    private final CoffeeBean moldy = new CoffeeBean("Moldy", 5);


    @Test
    @DisplayName("Successfully filters, maps, and sorts a standard mixed batch.")
    @Order(1)
    void testStandardProcessingPipeline() {
        List<Cherry> batch = new StreamEnforcingList<>(List.of(
                new Cherry(false, sumatra),
                new Cherry(true, buggy),
                new Cherry(false, ethiopia),
                new Cherry(true, moldy),
                new Cherry(false, colombia)
        ));
        verifyBasicKata(new BeanSorterKata(),
                batch,
                List.of(ethiopia, colombia, sumatra),
                Objects::equals);
    }

    @Test
    @DisplayName("An entirely defective batch returns an empty list without crashing.")
    @Order(2)
    void testAllDefectiveBatch() {
        List<Cherry> batch = new StreamEnforcingList<>(List.of(
                new Cherry(true, buggy),
                new Cherry(true, moldy)
        ));
        verifyBasicKata(new BeanSorterKata(),
                batch,
                List.of(),
                Objects::equals);
    }

    @Test
    @DisplayName("An empty conveyor belt returns an empty list.")
    @Order(3)
    void testEmptyBatch() {
        verifyBasicKata(new BeanSorterKata(),
                new StreamEnforcingList<>(List.of()),
                List.of(),
                Objects::equals);
    }

    @Test
    @DisplayName("Processes a batch that is already perfectly sorted (Descending).")
    @Order(4)
    void testAlreadySortedDescending() {
        List<Cherry> batch = new StreamEnforcingList<>(List.of(
                new Cherry(false, ethiopia),
                new Cherry(false, colombia),
                new Cherry(false, sumatra)
        ));
        verifyBasicKata(new BeanSorterKata(),
                batch,
                List.of(ethiopia, colombia, sumatra),
                Objects::equals);
    }

    @Test
    @DisplayName("Completely reverses a batch that arrives sorted Ascending.")
    @Order(5)
    void testSortedAscending() {
        List<Cherry> batch = new StreamEnforcingList<>(List.of(
                new Cherry(false, sumatra),
                new Cherry(false, colombia),
                new Cherry(false, ethiopia)
        ));
        verifyBasicKata(new BeanSorterKata(),
                batch,
                List.of(ethiopia, colombia, sumatra),
                Objects::equals);
    }

    @Test
    @DisplayName("Handles a batch where all beans have the exact same density score.")
    @Order(6)
    void testAllIdenticalDensityScores() {
        List<Cherry> batch = new StreamEnforcingList<>(List.of(
                new Cherry(false, colombia),
                new Cherry(false, brazil),
                new Cherry(false, colombia)
        ));
        verifyBasicKata(new BeanSorterKata(),
                batch,
                List.of(colombia, brazil, colombia),
                Objects::equals);
    }

    @Test
    @DisplayName("Properly sorts ties in density score among other distinct values.")
    @Order(7)
    void testTiesInDensityScore() {
        List<Cherry> batch = new StreamEnforcingList<>(List.of(
                new Cherry(false, sumatra),
                new Cherry(false, colombia),
                new Cherry(false, ethiopia),
                new Cherry(false, brazil)
        ));
        verifyBasicKata(new BeanSorterKata(),
                batch,
                List.of(ethiopia, colombia, brazil, sumatra),
                Objects::equals);
    }

    @Test
    @DisplayName("Sorts a massive, randomized batch correctly.")
    @Order(8)
    void testRandomOrder() {
        List<Cherry> batch = new StreamEnforcingList<>(List.of(
                new Cherry(false, colombia),
                new Cherry(false, sumatra),
                new Cherry(false, ethiopia)
        ));
        verifyBasicKata(new BeanSorterKata(),
                batch,
                List.of(ethiopia, colombia, sumatra),
                Objects::equals);
    }


    @Test
    @DisplayName("Processes a batch containing exactly one good cherry.")
    @Order(9)
    void testSingleGoodCherry() {
        List<Cherry> batch = new StreamEnforcingList<>(List.of(
                new Cherry(false, ethiopia)
        ));
        verifyBasicKata(new BeanSorterKata(),
                batch,
                List.of(ethiopia),
                Objects::equals);
    }

    @Test
    @DisplayName("Processes a batch containing exactly one defective cherry.")
    @Order(10)
    void testSingleDefectiveCherry() {
        List<Cherry> batch = new StreamEnforcingList<>(List.of(
                new Cherry(true, moldy)
        ));
        verifyBasicKata(new BeanSorterKata(),
                batch,
                List.of(),
                Objects::equals);
    }

    @Test
    @DisplayName("Processes a perfectly alternating sequence of good and defective cherries.")
    @Order(11)
    void testAlternatingGoodAndBad() {
        List<Cherry> batch = new StreamEnforcingList<>(List.of(
                new Cherry(false, colombia),
                new Cherry(true, buggy),
                new Cherry(false, ethiopia),
                new Cherry(true, moldy),
                new Cherry(false, sumatra)
        ));
        verifyBasicKata(new BeanSorterKata(),
                batch,
                List.of(ethiopia, colombia, sumatra),
                Objects::equals);
    }

    @Test
    @DisplayName("Handles a bean with exactly 0 density score.")
    @Order(12)
    void testZeroDensityBean() {
        List<Cherry> batch = new StreamEnforcingList<>(List.of(
                new Cherry(false, robusta),
                new Cherry(false, sumatra)
        ));
        verifyBasicKata(new BeanSorterKata(),
                batch,
                List.of(sumatra, robusta),
                Objects::equals);
    }

    @Test
    @DisplayName("Handles a mutated bean with a negative density score.")
    @Order(13)
    void testNegativeDensityBean() {
        List<Cherry> batch = new StreamEnforcingList<>(List.of(
                new Cherry(false, mutant),
                new Cherry(false, robusta)
        ));
        verifyBasicKata(new BeanSorterKata(),
                batch,
                List.of(robusta, mutant),
                Objects::equals);
    }

    @Test
    @DisplayName("Handles a bean with the maximum possible integer density.")
    @Order(14)
    void testMaxIntDensityBean() {
        List<Cherry> batch = new StreamEnforcingList<>(List.of(
                new Cherry(false, ethiopia),
                new Cherry(false, alien)
        ));
        verifyBasicKata(new BeanSorterKata(),
                batch,
                List.of(alien, ethiopia), Objects::equals);
    }

    @Test
    @DisplayName("Handles a combination of extreme max, negative, and zero values simultaneously.")
    @Order(15)
    void testAllExtremesSimultaneously() {
        List<Cherry> batch = new StreamEnforcingList<>(List.of(
                new Cherry(false, robusta),
                new Cherry(false, alien),
                new Cherry(false, mutant)
        ));
        verifyBasicKata(new BeanSorterKata(),
                batch,
                List.of(alien, robusta, mutant),
                Objects::equals);
    }
}