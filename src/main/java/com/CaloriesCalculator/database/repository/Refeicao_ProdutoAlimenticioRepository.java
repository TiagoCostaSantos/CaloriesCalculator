package com.CaloriesCalculator.database.repository;

import com.CaloriesCalculator.database.entity.RefeicaoProdutoId;
import com.CaloriesCalculator.database.entity.Refeicao_ProdutoAlimenticioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Refeicao_ProdutoAlimenticioRepository extends JpaRepository<Refeicao_ProdutoAlimenticioEntity, RefeicaoProdutoId> {
}
