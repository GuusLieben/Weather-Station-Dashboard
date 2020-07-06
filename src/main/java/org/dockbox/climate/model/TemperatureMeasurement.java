package org.dockbox.climate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;

public class TemperatureMeasurement extends Measurement {

    private final double temperatureInside;
    private final double temperatureOutside;

    public TemperatureMeasurement(Timestamp timestamp, double inside, double outside) {
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
        return "Celsius";
    }

    @Override
    public String getSymbol() {
        return "°C";
    }

    @Override
    @JsonIgnore
    public double getMeasure() {
        return super.measure;
    }
}
