package io.code.vanguard.brew.cuncurrency;

import io.code.vanguard.brew.BasicKata;

import java.util.List;

import static io.code.vanguard.brew.cuncurrency.SensorMeshKata.Sensor;

public class SensorMeshKata implements BasicKata<List<Sensor>, Long> {

    public interface Sensor {
        boolean ping();
    }

    @Override
    public Long solve(List<Sensor> sensors) {
        return null;
    }
}