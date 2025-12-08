package com.CaloriesCalculator.database.repository;

import com.CaloriesCalculator.database.entity.FichaAlimentarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FichaAlimentarRepository extends JpaRepository<FichaAlimentarEntity, Long> {

    Optional<FichaAlimentarEntity> findByDataAndUsuario_Id (LocalDate date, Long usuarioId);

    List<FichaAlimentarEntity> findByUsuario_Id(Long usuarioId);
}
