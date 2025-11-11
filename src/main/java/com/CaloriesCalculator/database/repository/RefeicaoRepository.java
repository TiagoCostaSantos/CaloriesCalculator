package com.CaloriesCalculator.database.repository;

import com.CaloriesCalculator.database.entity.RefeicaoEntity;
import com.CaloriesCalculator.database.entity.Refeicao_ProdutoAlimenticioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefeicaoRepository extends JpaRepository<RefeicaoEntity, Long> {

    List<RefeicaoEntity> findByFichaAlimentar_Id(Long fichaAlimentarId);
}
