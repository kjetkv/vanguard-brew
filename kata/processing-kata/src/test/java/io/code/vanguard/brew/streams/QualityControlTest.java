package io.code.vanguard.brew.streams;

import io.code.vanguard.brew.BasicKataTestBase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;
import java.util.Optional;

import static io.code.vanguard.brew.StreamEnforcingList.list;
import static io.code.vanguard.brew.streams.QualityControlKata.EspressoShot;
import static io.code.vanguard.brew.streams.QualityControlKata.QualityReport;

@DisplayName("Stream - Quality Control")
@Tag("Stream")
public class QualityControlTest extends BasicKataTestBase {


    private final EspressoShot perfect = new EspressoShot("Colombia", 93, 25);
    private final EspressoShot hot = new EspressoShot("Ethiopia", 97, 25);
    private final EspressoShot cold = new EspressoShot("Sumatra", 89, 25);
    private final EspressoShot slow = new EspressoShot("Brazil", 93, 36);
    private final EspressoShot sourVietnam = new EspressoShot("Vietnam", 93, 19);
    private final EspressoShot sourHonduras = new EspressoShot("Honduras", 93, 15);
    private final EspressoShot borderTempLow = new EspressoShot("Kenya", 90, 25);
    private final EspressoShot borderTempHigh = new EspressoShot("Peru", 96, 25);

    @Test
    @DisplayName("Empty batch returns perfectly clean report.")
    @Order(1)
    void testEmptyBatch() {
        verifyBasicKata(
                new QualityControlKata(),
                list(),
                new QualityReport(true, false, Optional.empty()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Perfect shot yields a clean report.")
    @Order(2)
    void testSinglePerfectShot() {
        verifyBasicKata(
                new QualityControlKata(),
                list(perfect),
                new QualityReport(true, false, Optional.empty()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Single hot shot triggers temperature instability.")
    @Order(3)
    void testSingleHotShot() {
        verifyBasicKata(
                new QualityControlKata(),
                list(hot),
                new QualityReport(false, false, Optional.empty()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Single cold shot triggers temperature instability.")
    @Order(4)
    void testSingleColdShot() {
        verifyBasicKata(
                new QualityControlKata(),
                list(cold),
                new QualityReport(false, false, Optional.empty()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Single slow shot triggers pressure warning.")
    @Order(5)
    void testSingleSlowShot() {
        verifyBasicKata(
                new QualityControlKata(),
                list(slow),
                new QualityReport(true, true, Optional.empty()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Single sour shot is detected correctly.")
    @Order(6)
    void testSingleSourShot() {
        verifyBasicKata(
                new QualityControlKata(),
                list(sourVietnam),
                new QualityReport(true, false, Optional.of(sourVietnam)),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Detects first sour shot among multiple bad shots.")
    @Order(7)
    void testMultipleSourShots() {
        verifyBasicKata(
                new QualityControlKata(),
                list(perfect, sourVietnam, sourHonduras),
                new QualityReport(true, false, Optional.of(sourVietnam)),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Temperature fails immediately at the start of the batch.")
    @Order(8)
    void testHotShotFirst() {
        verifyBasicKata(
                new QualityControlKata(),
                list(hot, perfect, perfect),
                new QualityReport(false, false, Optional.empty()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Temperature fails at the very end of the batch.")
    @Order(9)
    void testHotShotLast() {
        verifyBasicKata(
                new QualityControlKata(),
                list(perfect, perfect, hot),
                new QualityReport(false, false, Optional.empty()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Pressure warning triggers immediately at the start.")
    @Order(10)
    void testSlowShotFirst() {
        verifyBasicKata(
                new QualityControlKata(),
                list(slow, perfect, perfect),
                new QualityReport(true, true, Optional.empty()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Pressure warning triggers at the very end.")
    @Order(11)
    void testSlowShotLast() {
        verifyBasicKata(
                new QualityControlKata(),
                list(perfect, perfect, slow),
                new QualityReport(true, true, Optional.empty()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Exact lower boundary for temperature is accepted.")
    @Order(12)
    void testBorderTempLow() {
        verifyBasicKata(
                new QualityControlKata(),
                list(borderTempLow),
                new QualityReport(true, false, Optional.empty()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Exact upper boundary for temperature is accepted.")
    @Order(13)
    void testBorderTempHigh() {
        verifyBasicKata(
                new QualityControlKata(),
                list(borderTempHigh),
                new QualityReport(true, false, Optional.empty()),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Disaster batch triggers all alarms simultaneously.")
    @Order(14)
    void testTotalDisasterBatch() {
        verifyBasicKata(
                new QualityControlKata(),
                list(hot, slow, sourHonduras),
                new QualityReport(false, true, Optional.of(sourHonduras)),
                Objects::equals
        );
    }

    @Test
    @DisplayName("Complex mixed batch isolates individual failures accurately.")
    @Order(15)
    void testComplexMixedBatch() {
        verifyBasicKata(
                new QualityControlKata(),
                list(perfect, borderTempLow, slow, borderTempHigh, sourVietnam, perfect),
                new QualityReport(true, true, Optional.of(sourVietnam)),
                Objects::equals
        );
    }
}
