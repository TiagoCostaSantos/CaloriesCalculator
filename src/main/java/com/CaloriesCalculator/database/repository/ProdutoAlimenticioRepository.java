package com.CaloriesCalculator.database.repository;

import com.CaloriesCalculator.database.entity.ProdutoAlimenticioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoAlimenticioRepository extends JpaRepository<ProdutoAlimenticioEntity, Long> {
}
