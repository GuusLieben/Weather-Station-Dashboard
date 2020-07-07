package org.dockbox.climate.model.mysql;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

@Entity
@Table(name = "particles")
@Getter
public class ParticleRow extends AbstractRow {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

    @Id
    @JsonIgnore
    private String recorded;

    private double pm10;

    @JsonProperty("pm2_5")
    private double pm25;

    @JsonProperty("timestamp")
    public Timestamp getTimestamp() {
        return Timestamp.valueOf(getLDT());
    }

    @Override
    public LocalDateTime getLDT() {
        return  LocalDateTime.parse(recorded, formatter);
    }
}
