package org.dockbox.climate.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.dockbox.climate.model.MeasurementValues.JsonProperties;

import java.sql.Timestamp;

public class HumidityMeasurement extends Measurement {

    public HumidityMeasurement(Timestamp timestamp, double measure) {
        super(timestamp, measure);
    }

    @Override
    public String getDisplayName() {
        return MeasurementValues.HUMIDITY.getDisplayName();
    }

    @Override
    public String getSymbol() {
        return MeasurementValues.HUMIDITY.getSymbol();
    }

    @Override
    public String getHumanReadableName() {
        return MeasurementValues.HUMIDITY.getHumanReadableName();
    }

    @Override
    public String getJsonProperty() {
        return JsonProperties.HUMIDITY;
    }

    @Override
    @JsonProperty(JsonProperties.HUMIDITY)
    public double getMeasure() {
        return super.measure;
    }
}
