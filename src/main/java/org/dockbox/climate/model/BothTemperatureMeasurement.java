package org.dockbox.climate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.dockbox.climate.model.MeasurementValues.JsonProperties;

import java.sql.Timestamp;

public class BothTemperatureMeasurement extends Measurement {

    private final double temperatureInside;
    private final double temperatureOutside;

    public BothTemperatureMeasurement(Timestamp timestamp, double inside, double outside) {
        super(timestamp, -1D);
        this.temperatureInside = inside;
        this.temperatureOutside = outside;
    }

    public double getTemperatureInside() {
        return temperatureInside;
    }

    public double getTemperatureOutside() {
        return temperatureOutside;
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
        return JsonProperties.TEMPERATURE_INSIDE + "," + JsonProperties.TEMPERATURE_OUTSIDE;
    }

    @Override
    @JsonIgnore
    public double getMeasure() {
        return super.measure;
    }
}
