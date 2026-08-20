package io.code.vanguard.brew.streams;

import io.code.vanguard.brew.BasicKata;

import java.util.List;
import java.util.Optional;

import static io.code.vanguard.brew.streams.QualityControlKata.EspressoShot;
import static io.code.vanguard.brew.streams.QualityControlKata.QualityReport;

public class QualityControlKata implements BasicKata<List<EspressoShot>, QualityReport> {

    public record EspressoShot(String beanOrigin, int temperatureC, int extractionTimeSec) { }

    public record QualityReport(boolean allTemperaturesStable,
                                boolean anyPressureWarnings,
                                Optional<EspressoShot> firstSourShot) { }

    @Override
    public QualityReport solve(List<EspressoShot> batch) {
        return null;
    }
}