package io.code.vanguard.brew.concurrency;

import io.code.vanguard.brew.BasicKata;
import io.code.vanguard.brew.BasicKataTestBase;
import io.code.vanguard.brew.cuncurrency.DispatcherKata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.code.vanguard.brew.cuncurrency.DispatcherKata.PackingMachine;
import static io.code.vanguard.brew.cuncurrency.DispatcherKata.Workload;

@DisplayName("Concurrency - The Dispatcher")
@Tag("Concurrency")
public class DispatcherTest extends BasicKataTestBase {

    private abstract static class AntiCheatMachine implements PackingMachine {
        private final AtomicInteger activeThreads = new AtomicInteger(0);
        private final Thread mainThread = Thread.currentThread();

        @Override
        public String pack(String orderId) {
            Thread current = Thread.currentThread();

            if (current.equals(mainThread)) {
                throw new IllegalStateException("ALARM: Order executed sequentially on main thread!");
            }
            if (current.getName().contains("ForkJoinPool") || current.getName().contains("commonPool")) {
                throw new IllegalStateException("ALARM: Parallel Stream detected! You must build a Dispatcher (ExecutorService).");
            }
            if (current.isVirtual()) {
                throw new IllegalStateException("ALARM: Virtual Threads detected! The physical packers require OS Threads.");
            }

            int active = activeThreads.incrementAndGet();
            if (active > 5) {
                throw new IllegalStateException("ALARM: Power grid overloaded! More than 5 threads active at once.");
            }

            try {
                return executeSafe(orderId);
            } finally {
                activeThreads.decrementAndGet();
            }
        }

        protected abstract String executeSafe(String orderId);
    }

    private static class StandardMachine extends AntiCheatMachine {
        @Override
        protected String executeSafe(String orderId) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
            return orderId + "-PACKED";
        }
    }

    private static class CapacityBarrierMachine extends AntiCheatMachine {
        private final CountDownLatch latch = new CountDownLatch(5);

        @Override
        protected String executeSafe(String orderId) {
            latch.countDown();
            try {
                if (!latch.await(1, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("ALARM: Thread pool capacity is less than 5!");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return orderId + "-PACKED";
        }
    }

    private static class FaultyMachine extends AntiCheatMachine {
        @Override
        protected String executeSafe(String orderId) {
            if (orderId.contains("BROKEN")) {
                throw new RuntimeException("Cardboard jammed!");
            }
            return orderId + "-PACKED";
        }
    }

    private static class InstantMachine extends AntiCheatMachine {
        @Override
        protected String executeSafe(String orderId) {
            return orderId + "-PACKED";
        }
    }

    private static class MixedLatencyMachine extends AntiCheatMachine {
        @Override
        protected String executeSafe(String orderId) {
            int delay = Integer.parseInt(orderId.replace("O-", "")) % 2 == 0 ? 50 : 0;
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
            }
            return orderId + "-PACKED";
        }
    }

    private static class NullHandlingMachine extends AntiCheatMachine {
        @Override
        protected String executeSafe(String orderId) {
            if (orderId == null) {
                return "NULL-PACKED";
            }
            return orderId + "-PACKED";
        }
    }

    private Workload createWorkload(int count, PackingMachine machine) {
        List<String> orders = IntStream.rangeClosed(1, count)
                .mapToObj(i -> "O-" + i)
                .collect(Collectors.toList());

        return new Workload(orders, machine);
    }

    private List<String> createExpected(int count) {
        return IntStream.rangeClosed(1, count)
                .mapToObj(i -> "O-" + i + "-PACKED")
                .collect(Collectors.toList());
    }

    private static class LeakDetectingMachine extends AntiCheatMachine {
        private final Set<Thread> workerThreads = ConcurrentHashMap.newKeySet();

        @Override
        protected String executeSafe(String orderId) {
            workerThreads.add(Thread.currentThread());
            return orderId + "-PACKED";
        }

        public boolean hasLeakedThreads() {
            try {
                Thread.sleep(100);
            } catch (InterruptedException _) {
            }

            return workerThreads.stream().anyMatch(Thread::isAlive);
        }
    }


    @Test
    @DisplayName("Successfully processes a standard batch while respecting throttle limits.")
    @Order(1)
    void testStandardBatch() {
        verifyBasicKata(
                new DispatcherKata(),
                createWorkload(20, new StandardMachine()),
                createExpected(20),
                Objects::equals);
    }

    @Test
    @DisplayName("Forces the Executor to have exactly a 5-thread capacity.")
    @Order(2)
    void testExactPoolSizeEnforcement() {
        verifyBasicKata(
                new DispatcherKata(),
                createWorkload(5, new CapacityBarrierMachine()),
                createExpected(5),
                Objects::equals);
    }

    @Test
    @DisplayName("Handles a completely empty workload safely.")
    @Order(3)
    void testEmptyBatch() {
        verifyBasicKata(
                new DispatcherKata(),
                createWorkload(0, new StandardMachine()),
                List.of(),
                Objects::equals);
    }

    @Test
    @DisplayName("Gracefully processes a single order in the pool.")
    @Order(4)
    void testSingleItemBatch() {
        verifyBasicKata(
                new DispatcherKata(),
                createWorkload(1, new StandardMachine()),
                createExpected(1),
                Objects::equals);
    }

    @Test
    @DisplayName("Gracefully catches hardware exceptions inside futures and returns a FAILED receipt.")
    @Order(5)
    void testFaultyOrderHandledGracefully() {
        Workload workload = new Workload(
                List.of("O-1", "O-BROKEN", "O-3"),
                new FaultyMachine());

        verifyBasicKata(
                new DispatcherKata(),
                workload,
                List.of("O-1-PACKED", "FAILED", "O-3-PACKED"),
                Objects::equals);
    }

    @Test
    @DisplayName("Maintains batch integrity even if every single order fails.")
    @Order(6)
    void testAllFaultyOrders() {
        Workload workload = new Workload(
                List.of("BROKEN-1", "BROKEN-2", "BROKEN-3"),
                new FaultyMachine());

        verifyBasicKata(
                new DispatcherKata(),
                workload,
                List.of("FAILED", "FAILED", "FAILED"),
                Objects::equals);
    }

    @Test
    @DisplayName("STRESS: Processes massive throughput safely by successfully throttling.")
    @Order(7)
    void testMassiveLoad() {
        verifyBasicKata(
                new DispatcherKata(),
                createWorkload(1000, new InstantMachine()),
                createExpected(1000),
                Objects::equals);
    }

    @Test
    @DisplayName("Processes orders correctly regardless of vast differences in latency.")
    @Order(8)
    void testMixedLatencyBatch() {
        verifyBasicKata(
                new DispatcherKata(),
                createWorkload(30, new MixedLatencyMachine()),
                createExpected(30),
                Objects::equals);
    }

    @Test
    @DisplayName("Handles instantaneous hardware safely within the pool.")
    @Order(9)
    void testInstantExecution() {
        verifyBasicKata(
                new DispatcherKata(),
                createWorkload(50, new InstantMachine()),
                createExpected(50),
                Objects::equals);
    }

    @Test
    @DisplayName("Safely processes completely null order strings.")
    @Order(10)
    void testNullOrderValues() {
        Workload workload = new Workload(
                Arrays.asList("O-1", null, "O-3"),
                new NullHandlingMachine());

        verifyBasicKata(new DispatcherKata(),
                workload,
                List.of("O-1-PACKED", "NULL-PACKED", "O-3-PACKED"),
                Objects::equals);
    }

    @Test
    @DisplayName("Safely packs highly repetitive or duplicate batch data.")
    @Order(11)
    void testDuplicateOrderIds() {
        Workload workload = new Workload(
                List.of("O-DUP", "O-DUP", "O-DUP"),
                new StandardMachine());

        verifyBasicKata(
                new DispatcherKata(),
                workload,
                List.of("O-DUP-PACKED", "O-DUP-PACKED", "O-DUP-PACKED"),
                Objects::equals);
    }

    @Test
    @DisplayName("Safely handles exceptionally large string payloads.")
    @Order(12)
    void testLargePayloads() {
        String giant = "O-" + "A".repeat(5000);

        verifyBasicKata(new DispatcherKata(),
                new Workload(List.of(giant), new StandardMachine()),
                List.of(giant + "-PACKED"),
                Objects::equals);
    }

    @Test
    @DisplayName("STRESS: Memory Leak Guard. Ensures Executor is properly shut down over massive successive runs.")
    @Order(13)
    void testMassiveSequentialReuses() {
        DispatcherKata kata = new DispatcherKata();

        IntStream.range(0, 50)
                .forEach(_ ->
                        verifyBasicKata(
                                kata,
                                createWorkload(15, new InstantMachine()),
                                createExpected(15),
                                Objects::equals));
    }

    @Test
    @DisplayName("Maintains order stability and integrity on uneven primes.")
    @Order(14)
    void testPrimeNumberBatch() {
        verifyBasicKata(
                new DispatcherKata(),
                createWorkload(47, new StandardMachine()),
                createExpected(47),
                Objects::equals);
    }

    @Test
    @DisplayName("Handles slower sustained operations safely without dropping orders.")
    @Order(15)
    void testLongRunningBatch() {
        Workload workload = createWorkload(10, orderId -> {
            try {
                Thread.sleep(40);
            } catch (InterruptedException _) {
            }

            return orderId + "-PACKED";
        });

        verifyBasicKata(
                new DispatcherKata(),
                workload,
                createExpected(10),
                Objects::equals);
    }

    @Test
    @DisplayName("Enforces that the Dispatcher is explicitly shut down.")
    @Order(16)
    void testExecutorIsShutDown() {
        LeakDetectingMachine trackingMachine = new LeakDetectingMachine();

        BasicKata<Workload, List<String>> leakGuardedKata = workload -> {
            DispatcherKata delegate = new DispatcherKata();
            List<String> result = delegate.solve(workload);

            if (trackingMachine.hasLeakedThreads()) {
                return List.of("FATAL_ERROR: EXECUTOR_MEMORY_LEAK_DETECTED");
            }

            return result;
        };

        verifyBasicKata(
                leakGuardedKata,
                createWorkload(10, trackingMachine),
                createExpected(10),
                Objects::equals
        );
    }
}
