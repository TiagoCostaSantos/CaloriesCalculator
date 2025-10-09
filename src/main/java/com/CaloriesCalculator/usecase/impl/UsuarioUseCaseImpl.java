package com.CaloriesCalculator.usecase.impl;

import com.CaloriesCalculator.database.entity.UsuarioEntity;
import com.CaloriesCalculator.database.repository.UsuarioRepository;
import com.CaloriesCalculator.model.UsuarioModel;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class UsuarioUseCaseImpl implements UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;

    public UsuarioUseCaseImpl(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional // executa operações com o banco de dados e se essa transação dar errado ele faz um rollback caso aconteça uma exceção (não salva se der erro)
    public void cadastrarUsuario(UsuarioModel usuarioModel) {
        // TODO UUID AO INVES DE ID (produto e usuario)
        UsuarioEntity Ue = new UsuarioEntity();
        Ue.setNome(usuarioModel.getNome());
        Ue.setSobrenome(usuarioModel.getSobrenome());
        // TODO CRIPTOGRAFAR SENHA PARA SALVAR
        Ue.setPassword(usuarioModel.getPassword());
        Ue.setEmail(usuarioModel.getEmail());
        Ue.setDataNascimento(usuarioModel.getDataNascimento());
        Ue.setDataCadastro(LocalDate.now());
        Ue.setAltura(usuarioModel.getAltura());
        Ue.setPeso(usuarioModel.getPeso());

        usuarioRepository.save(Ue);
        // falta FichaALimentarID
    }
}
