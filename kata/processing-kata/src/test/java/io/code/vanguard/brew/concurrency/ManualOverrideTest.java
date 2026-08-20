package io.code.vanguard.brew.concurrency;

import io.code.vanguard.brew.BasicKataTestBase;
import io.code.vanguard.brew.cuncurrency.ManualOverrideKata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.stream.IntStream;

import static io.code.vanguard.brew.cuncurrency.ManualOverrideKata.MachineTask;

@DisplayName("Concurrency - Manual Override")
@Tag("Concurrency")
public class ManualOverrideTest extends BasicKataTestBase {

    private abstract static class AntiCheatTask extends MachineTask {
        private final Thread callingThread = Thread.currentThread();
        private volatile boolean completed = false;

        @Override
        public void run() {
            if (Thread.currentThread().equals(callingThread)) {
                throw new IllegalStateException("ALARM: Task executed sequentially on the main thread!");
            }
            executeSafe();
        }

        protected abstract void executeSafe();

        @Override
        public boolean isCompleted() {
            return completed;
        }

        protected void markCompleted() {
            this.completed = true;
        }

        @Override
        public String toString() {
            return "MachineTask{" +
                    "callingThread=" + callingThread +
                    ", completed=" + completed +
                    '}';
        }
    }

    private static class StandardDelayTask extends AntiCheatTask {
        @Override
        protected void executeSafe() {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            markCompleted();
        }
    }

    private static class FastTask extends AntiCheatTask {
        @Override
        protected void executeSafe() {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            markCompleted();
        }
    }

    private static class MicroBurstTask extends AntiCheatTask {
        @Override
        protected void executeSafe() {
            for (int i = 0; i < 1000; i++) {
                Math.pow(i, 2);
            }
            markCompleted();
        }
    }

    private static class ExceptionThrowingTask extends AntiCheatTask {
        @Override
        protected void executeSafe() {
            throw new RuntimeException("Simulated mechanical failure!");
        }
    }

    private static class YieldingTask extends AntiCheatTask {
        @Override
        protected void executeSafe() {
            Thread.yield();
            markCompleted();
        }
    }

    private static class LongDelayTask extends AntiCheatTask {
        @Override
        protected void executeSafe() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            markCompleted();
        }
    }

    private static class InterruptedInternalTask extends AntiCheatTask {
        @Override
        protected void executeSafe() {
            Thread.currentThread().interrupt();
            if (Thread.currentThread().isInterrupted()) {
                markCompleted();
            }
        }
    }

    @Test
    @DisplayName("Executes a standard delayed task asynchronously and waits for completion.")
    @Order(1)
    void testStandardDelayTask() {
        verifyBasicKata(
                new ManualOverrideKata(),
                new StandardDelayTask(),
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Executes a very fast task asynchronously and waits for completion.")
    @Order(2)
    void testFastTask() {
        verifyBasicKata(
                new ManualOverrideKata(),
                new FastTask(),
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Executes a heavy CPU burst task asynchronously and waits for completion.")
    @Order(3)
    void testMicroBurstTask() {
        verifyBasicKata(
                new ManualOverrideKata(),
                new MicroBurstTask(),
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Safely handles a worker thread that crashes without bringing down the main thread.")
    @Order(4)
    void testExceptionThrowingTask() {
        verifyBasicKata(
                new ManualOverrideKata(),
                new ExceptionThrowingTask(),
                false,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Executes a yielding task asynchronously and waits for completion.")
    @Order(5)
    void testYieldingTask() {
        verifyBasicKata(
                new ManualOverrideKata(),
                new YieldingTask(),
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Executes a long-running task asynchronously and correctly blocks until finished.")
    @Order(6)
    void testLongDelayTask() {
        verifyBasicKata(
                new ManualOverrideKata(),
                new LongDelayTask(),
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Executes a task that safely interrupts itself internally.")
    @Order(7)
    void testInterruptedInternalTask() {
        verifyBasicKata(
                new ManualOverrideKata(),
                new InterruptedInternalTask(),
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Sequentially running multiple instances of the same task behaves safely.")
    @Order(8)
    void testMultipleSequentialTasks() {
        ManualOverrideKata kata = new ManualOverrideKata();

        verifyBasicKata(
                kata,
                new StandardDelayTask(),
                true,
                Objects::equals
        );

        verifyBasicKata(
                kata,
                new StandardDelayTask(),
                true,
                Objects::equals
        );
    }

    @Test
    @DisplayName("Executes a massive sequence of fast tasks stably.")
    @Order(9)
    void testMassiveSequentialExecution() {
        ManualOverrideKata kata = new ManualOverrideKata();

        IntStream.range(0, 25).forEach(_ -> verifyBasicKata(
                kata,
                new FastTask(),
                true,
                Objects::equals
        ));
    }

    @Test
    @DisplayName("Executes a massive sequence of yielding tasks stably.")
    @Order(10)
    void testMassiveYieldingExecution() {
        ManualOverrideKata kata = new ManualOverrideKata();

        IntStream.range(0, 25).forEach(_ -> verifyBasicKata(
                kata,
                new YieldingTask(),
                true,
                Objects::equals
        ));
    }
}