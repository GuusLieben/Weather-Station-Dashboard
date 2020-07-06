package org.dockbox.climate.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class PM25Measurement extends Measurement {

    public PM25Measurement(Timestamp timestamp, double measure) {
        super(timestamp, measure);
    }

    @Override
    public String getDisplayName() {
        return "2.5 Micrometer";
    }

    @Override
    public String getSymbol() {
        return "μm";
    }

    @Override
    @JsonProperty("pm25")
    public double getMeasure() {
        return super.measure;
    }
}
