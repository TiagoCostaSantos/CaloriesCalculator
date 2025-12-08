package com.CaloriesCalculator.database.repository;

import com.CaloriesCalculator.database.entity.DadosUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DadosUsuarioRepository extends JpaRepository<DadosUsuarioEntity, Long> {

    Optional<DadosUsuarioEntity> findByUsuario_Id(Long id);

}
