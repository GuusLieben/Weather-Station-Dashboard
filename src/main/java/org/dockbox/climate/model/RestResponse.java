package org.dockbox.climate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;

@Getter
public class RestResponse {

    @Getter
    public static final class LegendEntry {

        @JsonProperty("human_readable_name")
        private final String humanReadableName;
        @JsonProperty("display_name")
        private final String displayName;
        private final String symbol;

        public LegendEntry(Measurement measurement) {
            this.symbol = measurement.getSymbol();
            this.displayName = measurement.getDisplayName();
            this.humanReadableName = measurement.getHumanReadableName();
        }

        public LegendEntry(MeasurementValues value) {
            this.symbol = value.getSymbol();
            this.displayName = value.getDisplayName();
            this.humanReadableName = value.getHumanReadableName();
        }

        public LegendEntry(String displayName, String symbol, String humanReadableName) {
            this.displayName = displayName;
            this.symbol = symbol;
            this.humanReadableName = humanReadableName;
        }
    }

    private final Map<String, LegendEntry> legend;
    private final List<?> data;

    @JsonIgnore
    public static final Map<String, LegendEntry> FULL_PARTICLE_RES = new ConcurrentHashMap<>();
    static {
        put(FULL_PARTICLE_RES, MeasurementValues.PM10);
        put(FULL_PARTICLE_RES, MeasurementValues.PM25);
    }

    @JsonIgnore
    public static final Map<String, LegendEntry> FULL_TEMPERATURE_RES = new ConcurrentHashMap<>();
    static {
        put(FULL_TEMPERATURE_RES, MeasurementValues.HUMIDITY);
        put(FULL_TEMPERATURE_RES, MeasurementValues.LIGHT);
        put(FULL_TEMPERATURE_RES, MeasurementValues.PRESSURE);
        put(FULL_TEMPERATURE_RES, MeasurementValues.TEMPERATURE);
        put(FULL_TEMPERATURE_RES, MeasurementValues.TEMPERATURE_OUTSIDE);
        put(FULL_TEMPERATURE_RES, MeasurementValues.TEMPERATURE_INSIDE);
    }

    private static void put(Map<String, LegendEntry> map, MeasurementValues value) {
        map.put(value.getJsonPropertyName(), new LegendEntry(value));
    }

    public RestResponse(List<?> data, Map<String, LegendEntry> legend) {
        this.legend = legend;
        this.data = data;
    }

    public RestResponse(List<?> data) {
        this.data = data;
        this.legend = new ConcurrentHashMap<>();
    }

    public void addToLegend(Measurement measurement) {
        this.legend.put(measurement.getJsonProperty(), new LegendEntry(measurement));
    }
}
