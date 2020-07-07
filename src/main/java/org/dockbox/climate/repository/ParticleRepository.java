package org.dockbox.climate.repository;

import org.dockbox.climate.model.mysql.ParticleRow;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface ParticleRepository extends Repository<ParticleRow, String> {

    List<ParticleRow> findByPm10GreaterThanAndPm10LessThan(double above, double below);

    List<ParticleRow> findByPm25GreaterThanAndPm25LessThan(double above, double below);

}
