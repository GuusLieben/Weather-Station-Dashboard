package org.dockbox.climate.repository;

import org.dockbox.climate.model.mysql.TemperatureRow;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface TemperatureRepository extends Repository<TemperatureRow, Integer> {

    List<TemperatureRow> findByLightGreaterThanAndLightLessThan(int above, int below);

    List<TemperatureRow> findByPressureGreaterThanAndPressureLessThan(double above, double below);

    List<TemperatureRow> findByTemp1GreaterThanAndTemp2GreaterThanAndTemp1LessThanAndTemp2LessThan(double temp1Above, double temp2Above, double temp1Below, double temp2Below);

    List<TemperatureRow> findByHumidityGreaterThanAndHumidityLessThan(double above, double below);

}
