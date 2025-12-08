package com.CaloriesCalculator.usecase.impl;

import com.CaloriesCalculator.database.entity.*;
import com.CaloriesCalculator.database.repository.*;
import com.CaloriesCalculator.model.DadosUsuarioModel;
import com.CaloriesCalculator.model.UsuarioModel;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class UsuarioUseCaseImpl implements UsuarioUseCase {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final DadosUsuarioRepository dadosUsuarioRepository;
    private final CalculosUseCaseImpl calculosUseCase;


    public UsuarioUseCaseImpl(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, DadosUsuarioRepository dadosUsuarioRepository, CalculosUseCaseImpl calculosUseCase){
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.dadosUsuarioRepository = dadosUsuarioRepository;
        this.calculosUseCase = calculosUseCase;
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

        Optional<UsuarioEntity> user = buscarPorEmail(email);
        UsuarioEntity usuarioExistente = user.get();

        usuarioExistente.setNome(usuarioModel.getNome());
        usuarioExistente.setSobrenome(usuarioModel.getSobrenome());
        usuarioExistente.setDataNascimento(usuarioModel.getDataNascimento());
        if(!usuarioModel.getPassword().isEmpty()){
            usuarioExistente.setPassword(passwordEncoder.encode(usuarioModel.getPassword()));
        }
        usuarioRepository.save(usuarioExistente);
    }

    @Override
    public Optional<UsuarioEntity> buscarPorEmail(String email){
        return usuarioRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public void salvarDadosUsuario(DadosUsuarioModel dadosUsuarioModel){

        UsuarioEntity user = buscarUsuarioLogado();
        Optional<DadosUsuarioEntity> dadosUsuario1 = buscarDadosUsuario(user.getId());

        DadosUsuarioEntity dadosUsuarioEntity = (dadosUsuario1.isPresent() ? dadosUsuario1.get() : new DadosUsuarioEntity());

        if (!dadosUsuario1.isPresent()) {
            dadosUsuarioEntity.setUsuario(user);
        }

        dadosUsuarioEntity.setPeso(dadosUsuarioModel.getPeso());
        dadosUsuarioEntity.setAltura(dadosUsuarioModel.getAltura());
        dadosUsuarioEntity.setSexo(dadosUsuarioModel.getSexo());
        dadosUsuarioEntity.setNivelAtividadeFisica(dadosUsuarioModel.getNivelAtividadeFisica());
        dadosUsuarioEntity.setMetaPeso(dadosUsuarioModel.getMeta());
        dadosUsuarioEntity.setIntensidade(dadosUsuarioModel.getIntensidade());
        dadosUsuarioEntity.setDataCadastroDados(LocalDate.now());
        dadosUsuarioRepository.save(dadosUsuarioEntity);
    }

    @Override
    public Optional<DadosUsuarioEntity> buscarDadosUsuario(Long id){
        Optional<DadosUsuarioEntity> dadosUsuarioEntity = dadosUsuarioRepository.findByUsuario_Id(id);
        return dadosUsuarioEntity;
    }

    @Override
    public UsuarioEntity buscarUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }
        Optional<UsuarioEntity> usuario = buscarPorEmail(email);
        UsuarioEntity user = usuario.get();
        return user;
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

