package com.CaloriesCalculator.usecase.impl;

import com.CaloriesCalculator.database.entity.*;
import com.CaloriesCalculator.database.repository.*;
import com.CaloriesCalculator.model.TipoRefeicaoEnum;
import com.CaloriesCalculator.model.UsuarioModel;
import com.CaloriesCalculator.usecase.ProdutoAlimenticioUseCase;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioUseCaseImpl implements UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final FichaAlimentarRepository fichaAlimentarRepository;
    private final RefeicaoRepository refeicaoRepository;
    private final Refeicao_ProdutoAlimenticioRepository refeicao_ProdutoAlimenticioRepository;
    private final ProdutoAlimenticioUseCase produtoAlimenticioUseCase;

    public UsuarioUseCaseImpl(UsuarioRepository usuarioRepository, ProdutoAlimenticioUseCase produtoAlimenticioUseCase, PasswordEncoder passwordEncoder, FichaAlimentarRepository fichaAlimentarRepository, RefeicaoRepository refeicaoRepository, Refeicao_ProdutoAlimenticioRepository refeicao_ProdutoAlimenticioRepository){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.refeicaoRepository = refeicaoRepository;
        this.fichaAlimentarRepository = fichaAlimentarRepository;
        this.refeicao_ProdutoAlimenticioRepository = refeicao_ProdutoAlimenticioRepository;
        this.produtoAlimenticioUseCase = produtoAlimenticioUseCase;
    }

    @Override
    @Transactional // executa operações com o banco de dados e se essa transação dar errado ele faz um rollback caso aconteça uma exceção (não salva se der erro)
    public boolean cadastrarUsuario(UsuarioModel usuarioModel) {
        boolean usuarioExist = buscarPorEmail(usuarioModel.getEmail()).isPresent();
        if (usuarioExist){
            return false;
        }
            UsuarioEntity Ue = new UsuarioEntity();
            Ue.setNome(usuarioModel.getNome());
            Ue.setSobrenome(usuarioModel.getSobrenome());
            Ue.setPassword(passwordEncoder.encode(usuarioModel.getPassword()));
            Ue.setEmail(usuarioModel.getEmail());
            Ue.setDataNascimento(usuarioModel.getDataNascimento());
            Ue.setDataCadastro(LocalDate.now());
            usuarioRepository.save(Ue);
            return true;
            // falta FichaALimentarID
    }
    @Override
    @Transactional
    public void atualizarUsuario(UsuarioModel usuarioModel, String email){

        Optional<UsuarioEntity> user = buscarPorEmail(email); // todo acrescentar orelsetrowh
        UsuarioEntity usuarioExistente = user.get();

        usuarioExistente.setNome(usuarioModel.getNome());
        usuarioExistente.setSobrenome(usuarioModel.getSobrenome());
        if(!usuarioModel.getPassword().isEmpty()){
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioModel.getPassword()));
        }
        usuarioRepository.save(usuarioExistente);
    }

    @Override
    public Optional<UsuarioEntity> buscarPorEmail(String email){
        return usuarioRepository.findByEmail(email);
    }

}

// todo lançar excessões se caso der errado conexões com o banco
/*
try {
UsuarioModel salvo = usuarioRepository.save(usuarioExistente);

    if (salvo != null && salvo.getId() != null) {
        return salvo; // ou retorna ResponseEntity.ok() no controller
    } else {
            throw new RuntimeException("Falha ao salvar o usuário");
    }
            } catch (Exception e) {
        throw new RuntimeException("Erro ao atualizar o usuário: " + e.getMessage());
        }

 */

