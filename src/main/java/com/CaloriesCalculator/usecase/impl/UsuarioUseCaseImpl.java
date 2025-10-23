package com.CaloriesCalculator.usecase.impl;

import com.CaloriesCalculator.database.entity.UsuarioEntity;
import com.CaloriesCalculator.database.repository.UsuarioRepository;
import com.CaloriesCalculator.model.UsuarioModel;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class UsuarioUseCaseImpl implements UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioUseCaseImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional // executa operações com o banco de dados e se essa transação dar errado ele faz um rollback caso aconteça uma exceção (não salva se der erro)
    public void cadastrarUsuario(UsuarioModel usuarioModel) {
        // TODO UUID AO INVES DE ID (produto e usuario) - por enquanto não precisa
        UsuarioEntity Ue = new UsuarioEntity();
        Ue.setNome(usuarioModel.getNome());
        Ue.setSobrenome(usuarioModel.getSobrenome());
        Ue.setPassword(passwordEncoder.encode(usuarioModel.getPassword()));
        Ue.setEmail(usuarioModel.getEmail());
        Ue.setDataNascimento(usuarioModel.getDataNascimento());
        Ue.setDataCadastro(LocalDate.now());
        Ue.setAltura(usuarioModel.getAltura());
        Ue.setPeso(usuarioModel.getPeso());

        usuarioRepository.save(Ue);
        // TODO FAZER JWT (TOKEN) DE ACESSO QUANDO CRIA?
        // falta FichaALimentarID
    }

    @Override
    public boolean autenticarUsuario(String email, String password){
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByEmail(email);
        if(usuarioOpt.isEmpty()){
            return false;
        }
        UsuarioEntity usuario = usuarioOpt.get();
        String passwordBanco = usuario.getPassword();
        // Faz a comparação segura: raw vs encoded
        return passwordEncoder.matches(password, passwordBanco);
    }
}
