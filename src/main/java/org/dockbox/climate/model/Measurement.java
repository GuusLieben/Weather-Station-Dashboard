package org.dockbox.climate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.sql.Timestamp;

import lombok.Getter;

@Getter
public abstract class Measurement {

    @JsonIgnore
    private final int id;
    private final Timestamp timestamp;

    @JsonIgnore
    protected final double measure;

    protected Measurement(Timestamp timestamp, double measure) {
        this.timestamp = timestamp;
        this.measure = measure;
        this.id = 0;
    }

    @JsonIgnore
    public abstract String getDisplayName();

    @JsonIgnore
    public abstract String getSymbol();

    @JsonIgnore
    public abstract String getHumanReadableName();

    @JsonIgnore
    public abstract String getJsonProperty();

    public abstract double getMeasure();
}
