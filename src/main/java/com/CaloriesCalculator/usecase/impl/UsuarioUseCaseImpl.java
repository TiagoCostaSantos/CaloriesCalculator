package com.CaloriesCalculator.usecase.impl;

import com.CaloriesCalculator.database.entity.*;
import com.CaloriesCalculator.database.repository.*;
import com.CaloriesCalculator.model.TipoRefeicaoEnum;
import com.CaloriesCalculator.model.UsuarioModel;
import com.CaloriesCalculator.usecase.ProdutoAlimenticioUseCase;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        // TODO UUID AO INVES DE ID (produto e usuario) - por enquanto não precisa

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

    @Override
    @Transactional
    public void salvarProdutoFichaAlimentar(String email, List<String> produtos, int refeicao){
        Optional<UsuarioEntity> usuario = buscarPorEmail(email);

        FichaAlimentarEntity fichaAlimentar = new FichaAlimentarEntity();
        fichaAlimentar.setData(LocalDate.now());
        fichaAlimentar.setUsuarioId(usuario.get());

        fichaAlimentarRepository.save(fichaAlimentar);

        RefeicaoEntity refeicaoEntity = new RefeicaoEntity();
        refeicaoEntity.setFichaAlimentar_id(fichaAlimentar);
        if(refeicao == 1){
            refeicaoEntity.setTipo(TipoRefeicaoEnum.CAFE);
        } else if (refeicao == 2) {
            refeicaoEntity.setTipo(TipoRefeicaoEnum.ALMOCO);
        } else if (refeicao == 3) {
            refeicaoEntity.setTipo(TipoRefeicaoEnum.LANCHE);
        } else if (refeicao == 4) {
            refeicaoEntity.setTipo(TipoRefeicaoEnum.JANTA);
        }else if (refeicao == 5){
            refeicaoEntity.setTipo(TipoRefeicaoEnum.SUPLEMENTO);
        }else{
            refeicaoEntity.setTipo(TipoRefeicaoEnum.OUTRO);
        }

        refeicaoRepository.save(refeicaoEntity);

        ProdutoAlimenticioEntity produto = produtoAlimenticioUseCase.buscarProdutoById2(1L);

        RefeicaoProdutoId refeicaoProdutoId = new RefeicaoProdutoId(refeicaoEntity.getId(), produto.getId()); // criar antes o produto

        Refeicao_ProdutoAlimenticioEntity refeicao_produtoAlimenticioEntity = new Refeicao_ProdutoAlimenticioEntity();
        refeicao_produtoAlimenticioEntity.setRefeicao(refeicaoEntity);
        refeicao_produtoAlimenticioEntity.setId(refeicaoProdutoId);
        refeicao_produtoAlimenticioEntity.setQuantidade(3);
        refeicao_produtoAlimenticioEntity.setProdutoAlimenticio(produto);

        refeicao_ProdutoAlimenticioRepository.save(refeicao_produtoAlimenticioEntity);

        // TODO RETIRAR API (PEGAR DADOS E SALVAR EM UM SCRIPT PARA SEMPRE QUE RODAR TER OS DADOS)
        // TODO COLOCAR NO MODAL QUANTIDADE 

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