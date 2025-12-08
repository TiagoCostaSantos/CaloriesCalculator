package com.CaloriesCalculator.usecase;
import com.CaloriesCalculator.database.entity.DadosUsuarioEntity;
import com.CaloriesCalculator.database.entity.UsuarioEntity;
import com.CaloriesCalculator.model.DadosUsuarioModel;
import com.CaloriesCalculator.model.UsuarioModel;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface UsuarioUseCase {

    boolean cadastrarUsuario(UsuarioModel usuarioModel);

    Optional<UsuarioEntity> buscarPorEmail(String email);

    void atualizarUsuario(UsuarioModel usuarioModel,String email);

    void salvarDadosUsuario(DadosUsuarioModel dadosUsuarioModel);

    UsuarioEntity buscarUsuarioLogado();

    Optional<DadosUsuarioEntity> buscarDadosUsuario(Long id);
}
