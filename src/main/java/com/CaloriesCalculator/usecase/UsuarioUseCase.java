package com.CaloriesCalculator.usecase;
import com.CaloriesCalculator.database.entity.UsuarioEntity;
import com.CaloriesCalculator.model.UsuarioModel;

import java.util.Optional;

public interface UsuarioUseCase {

    boolean cadastrarUsuario(UsuarioModel usuarioModel);

    Optional<UsuarioEntity> buscarPorEmail(String email);

    void atualizarUsuario(UsuarioModel usuarioModel,String email);
}
