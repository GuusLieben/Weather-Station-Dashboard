package org.dockbox.climate.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class SideTemperatureMeasurement extends Measurement {

    public SideTemperatureMeasurement(Timestamp timestamp, double measure) {
        super(timestamp, measure);
    }

    @Override
    public String getDisplayName() {
        return "Celsius";
    }

    @Override
    public String getSymbol() {
        return "°C";
    }

    @Override
    @JsonProperty("temperature")
    public double getMeasure() {
        return super.measure;
    }
}
