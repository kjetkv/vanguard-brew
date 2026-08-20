package io.code.vanguard.brew.concurrency;

import io.code.vanguard.brew.BasicKataTestBase;
import io.code.vanguard.brew.cuncurrency.DualBoilerKata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static io.code.vanguard.brew.cuncurrency.DualBoilerKata.DualBoilerMachine;
import static io.code.vanguard.brew.cuncurrency.DualBoilerKata.Latte;

@DisplayName("Concurrency - Dual Boiler")
@Tag("Concurrency")
public class DualBoilerTest extends BasicKataTestBase {

    private abstract static class ConcurrentEnforcingMachine implements DualBoilerMachine {
        protected final CountDownLatch rendezvous = new CountDownLatch(2);
        private final Thread mainThread = Thread.currentThread();

        protected void enforceConcurrency(String component) {
            if (Thread.currentThread().equals(mainThread)) {
                throw new IllegalStateException("ALARM: " + component + " executed sequentially on main thread!");
            }
            rendezvous.countDown();
            try {
                if (!rendezvous.await(500, TimeUnit.MILLISECONDS)) {
                    throw new IllegalStateException("ALARM: Tasks ran sequentially! " + component + " timed out waiting.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrupted.");
            }
        }

        @Override
        public String extractEspresso() {
            enforceConcurrency("brewBoiler");
            return "perfectEspresso";
        }

        @Override
        public String steamMilk() {
            enforceConcurrency("steamBoiler");
            return "microfoamMilk";
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    private static class StandardMachine extends ConcurrentEnforcingMachine { }

    private static class RunawaySteamMachine extends ConcurrentEnforcingMachine {
        @Override
        public String steamMilk() {
            enforceConcurrency("steamBoiler");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException _) {
            }
            return "burntMilk";
        }
    }

    private static class RunawayBrewMachine extends ConcurrentEnforcingMachine {
        @Override
        public String extractEspresso() {
            enforceConcurrency("brewBoiler");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException _) {
            }
            return "overExtractedSludge";
        }
    }

    private static class DualRunawayMachine extends ConcurrentEnforcingMachine {
        @Override
        public String extractEspresso() {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException _) {
            }
            return "sludge";
        }

        @Override
        public String steamMilk() {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException _) {
            }
            return "burntMilk";
        }
    }

    private static class InstantMachine extends ConcurrentEnforcingMachine {
        @Override
        public String extractEspresso() {
            super.rendezvous.countDown();
            return "instantEspresso";
        }

        @Override
        public String steamMilk() {
            super.rendezvous.countDown();
            return "instantMilk";
        }
    }

    private static class NullMachine extends ConcurrentEnforcingMachine {
        @Override
        public String extractEspresso() {
            super.rendezvous.countDown();
            return null;
        }

        @Override
        public String steamMilk() {
            super.rendezvous.countDown();
            return null;
        }
    }

    private static class FaultySteamMachine extends ConcurrentEnforcingMachine {
        @Override
        public String steamMilk() {
            super.steamMilk();
            throw new RuntimeException("Steam wand exploded!");
        }
    }

    private static class FaultyBrewMachine extends ConcurrentEnforcingMachine {
        @Override
        public String extractEspresso() {
            super.extractEspresso();
            throw new RuntimeException("Pump jammed!");
        }
    }

    private static class DualFaultyMachine extends ConcurrentEnforcingMachine {
        @Override
        public String extractEspresso() {
            super.rendezvous.countDown();
            throw new RuntimeException("Total failure");
        }

        @Override
        public String steamMilk() {
            super.rendezvous.countDown();
            throw new RuntimeException("Total failure");
        }
    }

    private static class LongExtractionMachine extends ConcurrentEnforcingMachine {
        @Override
        public String extractEspresso() {
            super.extractEspresso();
            try {
                Thread.sleep(500);
            } catch (InterruptedException _) {
            }
            return "slowEspresso";
        }
    }

    private static class LongSteamingMachine extends ConcurrentEnforcingMachine {
        @Override
        public String steamMilk() {
            super.steamMilk();
            try {
                Thread.sleep(500);
            } catch (InterruptedException _) {
            }
            return "slowMilk";
        }
    }

    private static class NearTimeoutMachine extends ConcurrentEnforcingMachine {
        @Override
        public String extractEspresso() {
            super.extractEspresso();
            try {
                Thread.sleep(800);
            } catch (InterruptedException _) {
            }
            return "borderlineEspresso";
        }

        @Override
        public String steamMilk() {
            super.steamMilk();
            try {
                Thread.sleep(800);
            } catch (InterruptedException _) {
            }
            return "borderlineMilk";
        }
    }


    @Test
    @DisplayName("Successfully brews a Latte by running perfectly in parallel.")
    @Order(1)
    void testPerfectConcurrentExtraction() {
        verifyBasicKata(new DualBoilerKata(),
                new StandardMachine(),
                new Latte("perfectEspresso", "microfoamMilk"),
                Objects::equals);
    }

    @Test
    @DisplayName("Gracefully falls back when the steam boiler runs infinitely.")
    @Order(2)
    void testRunawaySteamBoilerIsContained() {
        verifyBasicKata(new DualBoilerKata(),
                new RunawaySteamMachine(),
                new Latte("perfectEspresso", "steamFailed"),
                Objects::equals);
    }

    @Test
    @DisplayName("Gracefully falls back when the brew boiler runs infinitely.")
    @Order(3)
    void testRunawayBrewBoilerIsContained() {
        verifyBasicKata(new DualBoilerKata(),
                new RunawayBrewMachine(),
                new Latte("brewFailed", "microfoamMilk"),
                Objects::equals);
    }

    @Test
    @DisplayName("Gracefully handles a total simultaneous hardware lockup.")
    @Order(4)
    void testDualRunawayBoilers() {
        verifyBasicKata(new DualBoilerKata(),
                new DualRunawayMachine(),
                new Latte("brewFailed", "steamFailed"),
                Objects::equals);
    }

    @Test
    @DisplayName("Combines gracefully when hardware returns instantaneously.")
    @Order(5)
    void testInstantaneousExecution() {
        verifyBasicKata(new DualBoilerKata(),
                new InstantMachine(),
                new Latte("instantEspresso", "instantMilk"),
                Objects::equals);
    }


    @Test
    @DisplayName("Gracefully falls back when the steam hardware throws an exception.")
    @Order(6)
    void testFaultySteamHardwarePropagates() {
        verifyBasicKata(new DualBoilerKata(),
                new FaultySteamMachine(),
                new Latte("perfectEspresso", "steamFailed"),
                Objects::equals);
    }

    @Test
    @DisplayName("Gracefully falls back when the brew hardware throws an exception.")
    @Order(7)
    void testFaultyBrewHardwarePropagates() {
        verifyBasicKata(new DualBoilerKata(),
                new FaultyBrewMachine(),
                new Latte("brewFailed", "microfoamMilk"),
                Objects::equals);
    }

    @Test
    @DisplayName("Gracefully falls back when both systems explode simultaneously.")
    @Order(8)
    void testDualFaultyMachine() {
        verifyBasicKata(new DualBoilerKata(),
                new DualFaultyMachine(),
                new Latte("brewFailed", "steamFailed"),
                Objects::equals);
    }


    @Test
    @DisplayName("Processes correctly when brew extraction takes longer than steaming.")
    @Order(9)
    void testLongExtractionMachine() {
        verifyBasicKata(new DualBoilerKata(),
                new LongExtractionMachine(),
                new Latte("slowEspresso", "microfoamMilk"),
                Objects::equals);
    }

    @Test
    @DisplayName("Processes correctly when steaming takes longer than brew extraction.")
    @Order(10)
    void testLongSteamingMachine() {
        verifyBasicKata(new DualBoilerKata(),
                new LongSteamingMachine(),
                new Latte("perfectEspresso", "slowMilk"),
                Objects::equals);
    }

    @Test
    @DisplayName("Processes successfully right at the edge of the timeout limit.")
    @Order(11)
    void testNearTimeoutMachine() {
        verifyBasicKata(new DualBoilerKata(),
                new NearTimeoutMachine(),
                new Latte("borderlineEspresso", "borderlineMilk"),
                Objects::equals);
    }

    @Test
    @DisplayName("Maintains thread-pool integrity over a massive sequence.")
    @Order(12)
    void testMassiveSequentialExtractions() {
        DualBoilerKata kata = new DualBoilerKata();
        IntStream.range(0, 150).forEach(_ ->
                verifyBasicKata(kata,
                        new InstantMachine(),
                        new Latte("instantEspresso", "instantMilk"),
                        Objects::equals));
    }
}