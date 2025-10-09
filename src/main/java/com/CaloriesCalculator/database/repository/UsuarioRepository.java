package com.CaloriesCalculator.database.repository;
import com.CaloriesCalculator.database.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity,Long> {
}
