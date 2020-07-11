package org.dockbox.climate.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.dockbox.climate.model.MeasurementValues.JsonProperties;

import java.sql.Timestamp;

public class LightMeasurement extends Measurement {

    public LightMeasurement(Timestamp timestamp, double measure) {
        super(timestamp, measure);
    }

    @Override
    public String getDisplayName() {
        return MeasurementValues.LIGHT.getDisplayName();
    }

    @Override
    public String getSymbol() {
        return MeasurementValues.LIGHT.getSymbol();
    }

    @Override
    public String getHumanReadableName() {
        return MeasurementValues.LIGHT.getHumanReadableName();
    }

    @Override
    public String getJsonProperty() {
        return JsonProperties.LIGHT;
    }

    @Override
    @JsonProperty(JsonProperties.LIGHT)
    public double getMeasure() {
        return super.measure;
    }
}
