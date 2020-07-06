package org.dockbox.climate.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class LightMeasurement extends Measurement {

    public LightMeasurement(Timestamp timestamp, double measure) {
        super(timestamp, measure);
    }

    @Override
    public String getDisplayName() {
        return "Lux";
    }

    @Override
    public String getSymbol() {
        return "lx";
    }

    @Override
    @JsonProperty("lux")
    public double getMeasure() {
        return super.measure;
    }
}
