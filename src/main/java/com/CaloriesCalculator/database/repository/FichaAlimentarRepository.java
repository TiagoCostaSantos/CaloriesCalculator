package com.CaloriesCalculator.database.repository;

import com.CaloriesCalculator.database.entity.FichaAlimentarEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface FichaAlimentarRepository extends JpaRepository<FichaAlimentarEntity, Long> {

    List<FichaAlimentarEntity> findByDataAndUsuario_Id (LocalDate date, Long usuarioId);

}
