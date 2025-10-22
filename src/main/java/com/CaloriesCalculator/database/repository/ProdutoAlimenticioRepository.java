package com.CaloriesCalculator.database.repository;

import com.CaloriesCalculator.database.entity.ProdutoAlimenticioEntity;
import com.CaloriesCalculator.model.ProdutoAlimenticioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProdutoAlimenticioRepository extends JpaRepository<ProdutoAlimenticioEntity, Long> {

    // Containing para buscas maiores
    // IgnorCase ignora maiuscula e minusculas
    List<ProdutoAlimenticioEntity> findByTituloContainingIgnoreCase(String titulo);

}
