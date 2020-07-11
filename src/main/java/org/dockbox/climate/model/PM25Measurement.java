package org.dockbox.climate.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.dockbox.climate.model.MeasurementValues.JsonProperties;

import java.sql.Timestamp;

public class PM25Measurement extends Measurement {

    public PM25Measurement(Timestamp timestamp, double measure) {
        super(timestamp, measure);
    }

    @Override
    public String getDisplayName() {
        return MeasurementValues.PM25.getDisplayName();
    }

    @Override
    public String getSymbol() {
        return MeasurementValues.PM25.getSymbol();
    }

    @Override
    public String getHumanReadableName() {
        return MeasurementValues.PM25.getHumanReadableName();
    }

    @Override
    public String getJsonProperty() {
        return JsonProperties.PM25;
    }

    @Override
    @JsonProperty(JsonProperties.PM25)
    public double getMeasure() {
        return super.measure;
    }
}
