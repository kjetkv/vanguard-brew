package io.code.vanguard.brew.exception;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static io.code.vanguard.brew.Validators.verifySameExceptionClassAndMessage;
import static io.code.vanguard.brew.exception.MultiSensorMeltdownKata.BoilerSensor;
import static io.code.vanguard.brew.exception.MultiSensorMeltdownKata.DepressurizationException;
import static io.code.vanguard.brew.exception.MultiSensorMeltdownKata.GeneralPowerFaultException;
import static io.code.vanguard.brew.exception.MultiSensorMeltdownKata.OverheatingException;

@DisplayName("Exceptions - Multi-Sensor Meltdown")
@Tag("Resiliency")
@Tag("Exceptions")
public class MultiSensorMeltdownTest extends BasicKataTestBase {

    private static class MockBoilerSensor implements BoilerSensor {
        private final String nominalStatus;
        private final RuntimeException runtimeFault;

        public MockBoilerSensor(String nominalStatus) {
            this.nominalStatus = nominalStatus;
            this.runtimeFault = null;
        }


        public MockBoilerSensor(RuntimeException runtimeFault) {
            this.nominalStatus = null;
            this.runtimeFault = runtimeFault;
        }

        @Override
        public String readStatus() throws OverheatingException, DepressurizationException {
            if (runtimeFault != null) {
                throw runtimeFault;
            }
            return nominalStatus;
        }

        @Override
        public String toString() {
            if (runtimeFault != null) {
                return "BoilerSensor[FATAL: " + runtimeFault.getClass().getSimpleName() + "]";
            }
            return "BoilerSensor[STATUS: " + nominalStatus + "]";
        }
    }

    @Test
    @DisplayName("Reads standard operating temperature and pressure securely.")
    @Order(1)
    void testReadsNominalStatus() {
        verifyBasicKata(
                new MultiSensorMeltdownKata(),
                new MockBoilerSensor("SYSTEM_NOMINAL"),
                "SYSTEM_NOMINAL",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Reads successful boiler ignition sequence without issue.")
    @Order(2)
    void testReadsIgnitionStatus() {
        verifyBasicKata(
                new MultiSensorMeltdownKata(),
                new MockBoilerSensor("IGNITION_SUCCESSFUL"),
                "IGNITION_SUCCESSFUL",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Reads stable steam generation during peak brewing capacity.")
    @Order(3)
    void testReadsPeakCapacityStatus() {
        verifyBasicKata(
                new MultiSensorMeltdownKata(),
                new MockBoilerSensor("STEAM_GENERATION_STABLE"),
                "STEAM_GENERATION_STABLE",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Intercepts a minor overheating event and activates emergency sprinklers.")
    @Order(4)
    void testCatchesMinorOverheating() {
        verifyBasicKata(
                new MultiSensorMeltdownKata(),
                new MockBoilerSensor(new OverheatingException("Core temp reached 110C.")),
                "SPRINKLERS_ACTIVATED",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Intercepts a critical overheating event and activates emergency sprinklers.")
    @Order(5)
    void testCatchesCriticalOverheating() {
        verifyBasicKata(
                new MultiSensorMeltdownKata(),
                new MockBoilerSensor(new OverheatingException("Core temp exceeded 150C. Meltdown imminent.")),
                "SPRINKLERS_ACTIVATED",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Intercepts a minor pressure leak and activates emergency sprinklers.")
    @Order(6)
    void testCatchesMinorDepressurization() {
        verifyBasicKata(
                new MultiSensorMeltdownKata(),
                new MockBoilerSensor(new DepressurizationException("Seal 4 leaking slightly.")),
                "SPRINKLERS_ACTIVATED",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Intercepts a catastrophic hull rupture and activates emergency sprinklers.")
    @Order(7)
    void testCatchesCriticalDepressurization() {
        verifyBasicKata(
                new MultiSensorMeltdownKata(),
                new MockBoilerSensor(new DepressurizationException("Main steam drum ruptured.")),
                "SPRINKLERS_ACTIVATED",
                Objects::equals
        );
    }

    @Test
    @DisplayName("Allows a local grid brownout to crash the pipeline naturally.")
    @Order(8)
    void testPropagatesLocalPowerFault() {
        verifyException(
                () -> new MultiSensorMeltdownKata().solve(new MockBoilerSensor(new GeneralPowerFaultException("Local brownout detected."))),
                new GeneralPowerFaultException("Local brownout detected."),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Allows a complete blackout to crash the pipeline so generators can engage.")
    @Order(9)
    void testPropagatesTotalBlackout() {
        verifyException(
                () -> new MultiSensorMeltdownKata().solve(new MockBoilerSensor(new GeneralPowerFaultException("Total blackout across sector 7."))),
                new GeneralPowerFaultException("Total blackout across sector 7."),
                verifySameExceptionClassAndMessage
        );
    }

    @Test
    @DisplayName("Allows a catastrophic electrical surge to crash the pipeline.")
    @Order(10)
    void testPropagatesElectricalSurge() {
        verifyException(
                () -> new MultiSensorMeltdownKata().solve(new MockBoilerSensor(new GeneralPowerFaultException("Massive surge destroyed relay switch."))),
                new GeneralPowerFaultException("Massive surge destroyed relay switch."),
                verifySameExceptionClassAndMessage
        );
    }
}