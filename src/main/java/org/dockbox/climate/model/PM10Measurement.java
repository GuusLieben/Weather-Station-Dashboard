package org.dockbox.climate.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.dockbox.climate.model.MeasurementValues.JsonProperties;

import java.sql.Timestamp;

public class PM10Measurement extends Measurement {

    public PM10Measurement(Timestamp timestamp, double measure) {
        super(timestamp, measure);
    }

    @Override
    public String getDisplayName() {
        return MeasurementValues.PM10.getDisplayName();
    }

    @Override
    public String getSymbol() {
        return MeasurementValues.PM10.getSymbol();
    }

    @Override
    public String getHumanReadableName() {
        return MeasurementValues.PM10.getHumanReadableName();
    }

    @Override
    public String getJsonProperty() {
        return JsonProperties.PM10;
    }

    @Override
    @JsonProperty(JsonProperties.PM10)
    public double getMeasure() {
        return super.measure;
    }
}
