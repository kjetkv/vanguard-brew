package io.code.vanguard.brew.exception;

import io.code.vanguard.brew.BasicKata;

import static io.code.vanguard.brew.exception.UncalibratedScaleKata.HardwareScale;

public class UncalibratedScaleKata implements BasicKata<HardwareScale, Integer> {

    public static class SensorCalibrationException extends Exception {
        public SensorCalibrationException(String message) {
            super(message);
        }
    }

    @FunctionalInterface
    public interface HardwareScale {
        int readMass() throws SensorCalibrationException;
    }

    @Override
    public Integer solve(HardwareScale scale) {
        return null;
    }
}