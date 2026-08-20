package io.code.vanguard.brew.concurrency;

import io.code.vanguard.brew.BasicKataTestBase;
import io.code.vanguard.brew.cuncurrency.SynchronizedHopperKata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.code.vanguard.brew.cuncurrency.SynchronizedHopperKata.BeanSack;

@DisplayName("Concurrency - Synchronized Hopper")
@Tag("Concurrency")
public class SynchronizedHopperTest extends BasicKataTestBase {

    private record AntiCheatSack(int amount, Thread mainThread) implements BeanSack {

        @Override
        public int pour() {
            Thread current = Thread.currentThread();

            if (current.equals(mainThread)) {
                throw new IllegalStateException("ALARM: Sacks must be poured by worker threads, not the main thread!");
            }
            String currentThreadName = current.getName();
            if (currentThreadName.contains("ForkJoinPool") || currentThreadName.contains("commonPool") || current.isVirtual()) {
                throw new IllegalStateException("ALARM: High-level streams detected! The manual hopper requires bare-metal Threads.");
            }

            Thread.yield();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            return amount;
        }
    }

    private List<BeanSack> createBatch(int numberOfSacks, int beansPerSack) {
        return IntStream.range(0, numberOfSacks)
                .mapToObj(_ -> new AntiCheatSack(beansPerSack, Thread.currentThread()))
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("Aggregates a small standard batch correctly.")
    @Order(1)
    void testSmallStandardBatch() {
        verifyBasicKata(
                new SynchronizedHopperKata(),
                createBatch(10, 100),
                1000,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Returns zero when the hopper receives an empty list of sacks.")
    @Order(2)
    void testEmptyBatch() {
        verifyBasicKata(
                new SynchronizedHopperKata(),
                createBatch(0, 100),
                0,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Aggregates exactly one sack correctly.")
    @Order(3)
    void testSingleSack() {
        verifyBasicKata(
                new SynchronizedHopperKata(),
                createBatch(1, 500),
                500,
                Objects::equals
        );
    }

    @Test
    @DisplayName("High contention with 500 threads adding 1 bean.")
    @Order(4)
    void testHighContentionMicroBatch() {
        verifyBasicKata(
                new SynchronizedHopperKata(),
                createBatch(500, 1),
                500,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Extreme contention with 2000 threads.")
    @Order(5)
    void testExtremeContention() {
        verifyBasicKata(
                new SynchronizedHopperKata(),
                createBatch(2000, 5),
                10000,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Aggregates properly when sacks contain zero beans.")
    @Order(6)
    void testZeroBeanSacks() {
        verifyBasicKata(
                new SynchronizedHopperKata(),
                createBatch(100, 0),
                0,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Aggregates a mixed-value batch accurately.")
    @Order(7)
    void testMixedBatch() {

        Thread current = Thread.currentThread();

        verifyBasicKata(
                new SynchronizedHopperKata(),
                List.of(
                        new AntiCheatSack(10, current),
                        new AntiCheatSack(20, current),
                        new AntiCheatSack(30, current),
                        new AntiCheatSack(40, current),
                        new AntiCheatSack(50, current)
                ),
                150,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Maintains state stability across multiple sequential test runs.")
    @Order(8)
    void testSequentialReuse() {
        SynchronizedHopperKata sharedKata = new SynchronizedHopperKata();

        verifyBasicKata(
                sharedKata,
                createBatch(50, 2),
                100,
                Objects::equals
        );

        verifyBasicKata(
                sharedKata,
                createBatch(50, 2),
                100,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Handles massive individual sack values safely.")
    @Order(9)
    void testMassiveIndividualSackValues() {
        verifyBasicKata(
                new SynchronizedHopperKata(),
                createBatch(10, 100000),
                1000000,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Handles a single thread doing massive amounts of work.")
    @Order(10)
    void testSingleMassiveThread() {
        verifyBasicKata(
                new SynchronizedHopperKata(),
                createBatch(1, 2000000000),
                2000000000,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Handles two intensely competing threads exactly.")
    @Order(11)
    void testTwoIntenseCompetitors() {
        verifyBasicKata(
                new SynchronizedHopperKata(),
                createBatch(2, 500000),
                1000000,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Aggregates properly when interleaving heavy and light sacks.")
    @Order(12)
    void testInterleavedWeights() {
        Thread main = Thread.currentThread();

        List<BeanSack> batch = IntStream.range(0, 50)
                .mapToObj(i -> new AntiCheatSack(i % 2 == 0 ? 1 : 1000, main))
                .collect(Collectors.toList());

        verifyBasicKata(
                new SynchronizedHopperKata(),
                batch,
                25025,
                Objects::equals
        );
    }


    @Test
    @DisplayName("Processes a massive prime number payload flawlessly.")
    @Order(13)
    void testPrimeNumberPayload() {
        verifyBasicKata(
                new SynchronizedHopperKata(),
                createBatch(97, 89),
                8633,
                Objects::equals
        );
    }
}