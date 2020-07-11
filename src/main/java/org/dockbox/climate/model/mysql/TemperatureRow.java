package org.dockbox.climate.model.mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.dockbox.climate.model.MeasurementValues.JsonProperties;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name = "temperatures")
@Getter
public class TemperatureRow extends AbstractRow {

    @JsonProperty(JsonProperties.LIGHT)
    private int light;

    @Column(name = "Datum")
    @JsonIgnore
    private Date date;

    @Column(name = "Tijd")
    @JsonIgnore
    private Time time;

    @JsonProperty(JsonProperties.TEMPERATURE_OUTSIDE)
    private double temp1;
    @JsonProperty(JsonProperties.TEMPERATURE_INSIDE)
    private double temp2;

    @Column(name = "humi")
    @JsonProperty(JsonProperties.HUMIDITY)
    private double humidity;

    @Column(name = "press")
    @JsonProperty(JsonProperties.PRESSURE)
    private double pressure;

    @Id
    @Column(name = "ID")
    @JsonIgnore
    private int id;

    @JsonProperty("timestamp")
    public Timestamp getTimestamp() {
        Date date = getDate();
        Time time = getTime();
        LocalDate localDate = date.toLocalDate();
        LocalTime localTime = time.toLocalTime().plus(1, ChronoUnit.HOURS); // Time difference from UTC
        LocalDateTime ldt = LocalDateTime.of(localDate, localTime);
        return Timestamp.valueOf(ldt);
    }

    @Override
    public String toString() {
        return "TemperatureRow{" +
                "light=" + light +
                ", date=" + date +
                ", temp1=" + temp1 +
                ", temp2=" + temp2 +
                ", humidity=" + humidity +
                ", pressure=" + pressure +
                ", time=" + time +
                ", id=" + id +
                '}';
    }

    @Override
    public LocalDateTime getLDT() {
        LocalDate localDate = date.toLocalDate();
        LocalTime localTime = time.toLocalTime().plus(1, ChronoUnit.HOURS); // Time difference from UTC
        return LocalDateTime.of(localDate, localTime);
    }
}
