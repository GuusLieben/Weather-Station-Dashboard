package org.dockbox.climate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
