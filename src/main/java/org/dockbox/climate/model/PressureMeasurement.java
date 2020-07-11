package org.dockbox.climate.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.dockbox.climate.model.MeasurementValues.JsonProperties;

import java.sql.Timestamp;

public class PressureMeasurement extends Measurement {

    public PressureMeasurement(Timestamp timestamp, double measure) {
        super(timestamp, measure);
    }

    @Override
    public String getDisplayName() {
        return MeasurementValues.PRESSURE.getDisplayName();
    }

    @Override
    public String getSymbol() {
        return MeasurementValues.PRESSURE.getSymbol();
    }

    @Override
    public String getHumanReadableName() {
        return MeasurementValues.PRESSURE.getHumanReadableName();
    }

    @Override
    public String getJsonProperty() {
        return JsonProperties.PRESSURE;
    }

    @Override
    @JsonProperty(JsonProperties.PRESSURE)
    public double getMeasure() {
        return super.measure;
    }
}
