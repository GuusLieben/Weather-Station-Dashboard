package org.dockbox.climate.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class HumidityMeasurement extends Measurement {

    public HumidityMeasurement(Timestamp timestamp, double measure) {
        super(timestamp, measure);
    }

    @Override
    public String getDisplayName() {
        return "Humidity";
    }

    @Override
    public String getSymbol() {
        return "%";
    }

    @Override
    @JsonProperty("humidity")
    public double getMeasure() {
        return super.measure;
    }
}
