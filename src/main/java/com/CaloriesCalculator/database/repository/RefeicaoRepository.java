package com.CaloriesCalculator.database.repository;

import com.CaloriesCalculator.database.entity.RefeicaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefeicaoRepository extends JpaRepository<RefeicaoEntity, Long> {
}
