package org.dockbox.climate.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class PM10Measurement extends Measurement {

    public PM10Measurement(Timestamp timestamp, double measure) {
        super(timestamp, measure);
    }

    @Override
    public String getDisplayName() {
        return "10 Micrometer";
    }

    @Override
    public String getSymbol() {
        return "μm";
    }

    @Override
    @JsonProperty("pm10")
    public double getMeasure() {
        return super.measure;
    }
}
