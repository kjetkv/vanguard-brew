package io.code.vanguard.brew.concurrency;

import io.code.vanguard.brew.BasicKataTestBase;
import io.code.vanguard.brew.cuncurrency.SensorMeshKata;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static io.code.vanguard.brew.cuncurrency.SensorMeshKata.Sensor;

@DisplayName("Concurrency - Sensor Mesh")
@Tag("Concurrency")
public class SensorMeshTest extends BasicKataTestBase {


    private abstract static class AntiCheatSensor implements Sensor {
        private final Thread mainThread = Thread.currentThread();

        @Override
        public boolean ping() {
            Thread current = Thread.currentThread();

            if (current.equals(mainThread)) {
                throw new IllegalStateException("ALARM: Roaster sensor pinged sequentially on the main control thread!");
            }
            if (!current.isVirtual()) {
                throw new IllegalStateException(
                        "ALARM: Heavy OS Platform Thread detected! " +
                                "The factory mesh network cannot support " + current.getClass().getSimpleName() +
                                ". You MUST use Virtual Threads (Executors.newVirtualThreadPerTaskExecutor) to avoid crashing the grid."
                );
            }

            return executeSafe();
        }

        protected abstract boolean executeSafe();

        @Override
        public String toString() {
            return this.getClass().getSimpleName();
        }
    }

    private static class ActiveTelemetrySensor extends AntiCheatSensor {
        @Override
        protected boolean executeSafe() {
            try {
                Thread.sleep(5);
            } catch (InterruptedException _) {
            }
            return true;
        }
    }

    private static class OfflineTelemetrySensor extends AntiCheatSensor {
        @Override
        protected boolean executeSafe() {
            try {
                Thread.sleep(5);
            } catch (InterruptedException _) {
            }
            return false;
        }
    }

    private static class BlownFuseSensor extends AntiCheatSensor {
        @Override
        protected boolean executeSafe() {
            throw new RuntimeException("Sensor short-circuited due to high roaster heat!");
        }
    }

    private static class InstantTelemetrySensor extends AntiCheatSensor {
        @Override
        protected boolean executeSafe() {
            return true;
        }
    }

    private static class HighLatencyValveSensor extends AntiCheatSensor {
        @Override
        protected boolean executeSafe() {
            try {
                Thread.sleep(200);
            } catch (InterruptedException _) {
            }
            return true;
        }
    }

    private static class MeshBarrierSensor extends AntiCheatSensor {
        private final CountDownLatch latch;

        public MeshBarrierSensor(CountDownLatch latch) {
            this.latch = latch;
        }

        @Override
        protected boolean executeSafe() {
            latch.countDown();
            try {
                if (!latch.await(2, TimeUnit.SECONDS)) {
                    throw new IllegalStateException("ALARM: Insufficient concurrency! Expected massive parallel mesh execution.");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            return true;
        }
    }

    private List<Sensor> createMeshZone(int count, Sensor sensorModel) {
        return IntStream.range(0, count)
                .mapToObj(_ -> sensorModel)
                .collect(Collectors.toList());
    }


    @Test
    @DisplayName("Aggregates a standard small zone of active roasting sensors.")
    @Order(1)
    void testStandardMeshZone() {
        verifyBasicKata(new SensorMeshKata(),
                createMeshZone(10, new ActiveTelemetrySensor()),
                10L,
                Objects::equals);
    }

    @Test
    @DisplayName("Handles a completely unpopulated sector safely (Zero sensors).")
    @Order(2)
    void testEmptyMeshSector() {
        verifyBasicKata(new SensorMeshKata(),
                new ArrayList<>(),
                0L,
                Objects::equals);
    }

    @Test
    @DisplayName("Aggregates a sector where every single telemetry unit is offline.")
    @Order(3)
    void testAllOfflineSector() {
        verifyBasicKata(new SensorMeshKata(),
                createMeshZone(25, new OfflineTelemetrySensor()),
                0L,
                Objects::equals);
    }

    @Test
    @DisplayName("Safely catches and isolates blown fuses without crashing the entire mesh reading.")
    @Order(4)
    void testBlownFusesHandledGracefully() {
        List<Sensor> meshZone = List.of(
                new ActiveTelemetrySensor(),
                new BlownFuseSensor(),
                new ActiveTelemetrySensor()
        );
        verifyBasicKata(new SensorMeshKata(),
                meshZone,
                2L,
                Objects::equals);
    }

    @Test
    @DisplayName("Maintains factory mesh integrity even if a catastrophic sector-wide surge blows all fuses.")
    @Order(5)
    void testSectorWideSurge() {
        verifyBasicKata(new SensorMeshKata(),
                createMeshZone(10, new BlownFuseSensor()),
                0L,
                Objects::equals);
    }

    @Test
    @DisplayName("Processes chaotic zone states (Active reading, Offline units, and Blown fuses mixed together).")
    @Order(6)
    void testChaoticZoneState() {
        List<Sensor> meshZone = new ArrayList<>();
        meshZone.addAll(createMeshZone(10, new ActiveTelemetrySensor()));
        meshZone.addAll(createMeshZone(5, new OfflineTelemetrySensor()));
        meshZone.addAll(createMeshZone(5, new BlownFuseSensor()));

        verifyBasicKata(new SensorMeshKata(),
                meshZone,
                10L,
                Objects::equals);
    }

    @Test
    @DisplayName("Handles virtually instantaneous local-network telemetry seamlessly.")
    @Order(7)
    void testInstantaneousTelemetry() {
        verifyBasicKata(new SensorMeshKata(),
                createMeshZone(100, new InstantTelemetrySensor()),
                100L,
                Objects::equals);
    }

    @Test
    @DisplayName("Handles very slow pressure-valve sensor readings cleanly.")
    @Order(8)
    void testHighLatencyValveSensors() {
        verifyBasicKata(new SensorMeshKata(),
                createMeshZone(50, new HighLatencyValveSensor()),
                50L,
                Objects::equals);
    }

    @Test
    @DisplayName("Pings 1,000 independent roaster sensors simultaneously with zero OS overhead.")
    @Order(9)
    void testOneThousandVirtualSensors() {
        verifyBasicKata(new SensorMeshKata(),
                createMeshZone(1000, new ActiveTelemetrySensor()),
                1000L,
                Objects::equals);
    }

    @Test
    @DisplayName("Pings 10,000 global warehouse sensors simultaneously.")
    @Order(10)
    void testTenThousandVirtualSensors() {
        verifyBasicKata(new SensorMeshKata(),
                createMeshZone(10000, new ActiveTelemetrySensor()),
                10000L,
                Objects::equals);
    }

    @Test
    @DisplayName("Pings 50,000 IoT delivery drone sensors.")
    @Order(11)
    void testFiftyThousandVirtualSensors() {
        verifyBasicKata(new SensorMeshKata(),
                createMeshZone(50000, new InstantTelemetrySensor()),
                50000L,
                Objects::equals);
    }

    @Test
    @DisplayName("Forces exactly 1,000 sensors to wait in parallel for the master roaster clock.")
    @Order(12)
    void testOneThousandConcurrentWaiters() {
        Sensor waitingSensor = new MeshBarrierSensor(new CountDownLatch(1000));

        verifyBasicKata(new SensorMeshKata(),
                createMeshZone(1000, waitingSensor),
                1000L,
                Objects::equals);
    }

    @Test
    @DisplayName("Runs successive enterprise-scale mesh scans to ensure brewing readiness.")
    @Order(13)
    void testSuccessiveEnterpriseMeshScans() {
        SensorMeshKata kata = new SensorMeshKata();
        IntStream.range(0, 10).forEach(_ ->
                verifyBasicKata(kata,
                        createMeshZone(5000, new InstantTelemetrySensor()),
                        5000L,
                        Objects::equals));
    }

    @Test
    @DisplayName("Processes effectively un-even or prime numbered sensor zone deployments.")
    @Order(14)
    void testPrimeNumberedDeployment() {
        verifyBasicKata(new SensorMeshKata(),
                createMeshZone(8923, new ActiveTelemetrySensor()),
                8923L,
                Objects::equals);
    }

    @Test
    @DisplayName("Safely aggregates a single espresso machine operating in complete network isolation.")
    @Order(15)
    void testIsolatedEspressoMachine() {
        verifyBasicKata(new SensorMeshKata(),
                createMeshZone(1, new ActiveTelemetrySensor()),
                1L,
                Objects::equals);
    }
}