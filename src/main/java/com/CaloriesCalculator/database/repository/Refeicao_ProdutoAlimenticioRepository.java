package com.CaloriesCalculator.database.repository;

import com.CaloriesCalculator.database.entity.FichaAlimentarEntity;
import com.CaloriesCalculator.database.entity.RefeicaoEntity;
import com.CaloriesCalculator.database.entity.RefeicaoProdutoId;
import com.CaloriesCalculator.database.entity.Refeicao_ProdutoAlimenticioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface Refeicao_ProdutoAlimenticioRepository extends JpaRepository<Refeicao_ProdutoAlimenticioEntity, RefeicaoProdutoId> {

    Optional<Refeicao_ProdutoAlimenticioEntity> findByRefeicao_IdAndProdutoAlimenticio_Id(Long refeicaoId, Long produtoAlimenticioId);
}
