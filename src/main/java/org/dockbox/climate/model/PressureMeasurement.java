package org.dockbox.climate.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class PressureMeasurement extends Measurement {

    public PressureMeasurement(Timestamp timestamp, double measure) {
        super(timestamp, measure);
    }

    @Override
    public String getDisplayName() {
        return "Hectopascal";
    }

    @Override
    public String getSymbol() {
        return "hPa";
    }

    @Override
    @JsonProperty("pressure")
    public double getMeasure() {
        return super.measure;
    }
}
