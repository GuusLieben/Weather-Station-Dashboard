package org.dockbox.climate.model;

public enum MeasurementValues {
    PM10("10 Micrometer", "μm", "Particulate Matter PM10", JsonProperties.PM10),
    PM25("2.5 Micrometer", "μm", "Particulate Matter PM2.5", JsonProperties.PM25),
    HUMIDITY("Humidity", "%", "Humidity", JsonProperties.HUMIDITY),
    LIGHT("Lux", "lx", "Light", JsonProperties.LIGHT),
    PRESSURE("Hectopascal", "hPa", "Pressure", JsonProperties.PRESSURE),
    TEMPERATURE("Celsius", "°C", "Temperature", JsonProperties.TEMPERATURE),
    TEMPERATURE_OUTSIDE("Celsius", "°C", "Temperature Outside", JsonProperties.TEMPERATURE_OUTSIDE),
    TEMPERATURE_INSIDE("Celsius", "°C", "Temperature Outside", JsonProperties.TEMPERATURE_INSIDE);
    private final String displayName;
    private final String symbol;
    private final String humanReadableName;
    private final String jsonPropertyName;

    MeasurementValues(String displayName, String symbol, String humanReadableName, String jsonPropertyName) {
        this.displayName = displayName;
        this.symbol = symbol;
        this.humanReadableName = humanReadableName;
        this.jsonPropertyName = jsonPropertyName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getHumanReadableName() {
        return humanReadableName;
    }

    public String getJsonPropertyName() {
        return jsonPropertyName;
    }

    public static class JsonProperties {
        public static final String PM10 = "pm10";
        public static final String PM25 = "pm25";
        public static final String HUMIDITY = "humidity";
        public static final String LIGHT = "lux";
        public static final String PRESSURE = "pressure";
        public static final String TEMPERATURE = "temperature";
        public static final String TEMPERATURE_OUTSIDE = "temperatureOutside";
        public static final String TEMPERATURE_INSIDE = "temperatureInside";
    }
}
