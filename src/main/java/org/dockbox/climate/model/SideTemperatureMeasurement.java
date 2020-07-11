package org.dockbox.climate.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.dockbox.climate.model.MeasurementValues.JsonProperties;

import java.sql.Timestamp;

public class SideTemperatureMeasurement extends Measurement {

    public SideTemperatureMeasurement(Timestamp timestamp, double measure) {
        super(timestamp, measure);
    }

    @Override
    public String getDisplayName() {
        return MeasurementValues.TEMPERATURE.getDisplayName();
    }

    @Override
    public String getSymbol() {
        return MeasurementValues.TEMPERATURE.getSymbol();
    }

    @Override
    public String getHumanReadableName() {
        return MeasurementValues.TEMPERATURE.getHumanReadableName();
    }

    @Override
    public String getJsonProperty() {
        return JsonProperties.TEMPERATURE;
    }

    @Override
    @JsonProperty(JsonProperties.TEMPERATURE)
    public double getMeasure() {
        return super.measure;
    }
}
