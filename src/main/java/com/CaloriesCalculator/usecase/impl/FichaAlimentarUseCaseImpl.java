package com.CaloriesCalculator.usecase.impl;

import com.CaloriesCalculator.database.entity.*;
import com.CaloriesCalculator.database.repository.FichaAlimentarRepository;
import com.CaloriesCalculator.database.repository.RefeicaoRepository;
import com.CaloriesCalculator.database.repository.Refeicao_ProdutoAlimenticioRepository;
import com.CaloriesCalculator.database.entity.TipoRefeicaoEnum;
import com.CaloriesCalculator.model.RefeicaoProdutoModel;
import com.CaloriesCalculator.usecase.FichaAlimentarUseCase;
import com.CaloriesCalculator.usecase.ProdutoAlimenticioUseCase;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FichaAlimentarUseCaseImpl implements FichaAlimentarUseCase {

    private final FichaAlimentarRepository fichaAlimentarRepository;
    private final RefeicaoRepository refeicaoRepository;
    private final Refeicao_ProdutoAlimenticioRepository refeicao_ProdutoAlimenticioRepository;
    private final ProdutoAlimenticioUseCase produtoAlimenticioUseCase;
    private final UsuarioUseCase usuarioUseCase;

    @Autowired
    private HttpSession session;


    public FichaAlimentarUseCaseImpl(UsuarioUseCase usuarioUseCase,FichaAlimentarRepository fichaAlimentarRepository, RefeicaoRepository refeicaoRepository,Refeicao_ProdutoAlimenticioRepository refeicao_ProdutoAlimenticioRepository,ProdutoAlimenticioUseCase produtoAlimenticioUseCase){
        this.fichaAlimentarRepository = fichaAlimentarRepository;
        this.refeicaoRepository = refeicaoRepository;
        this.refeicao_ProdutoAlimenticioRepository = refeicao_ProdutoAlimenticioRepository;
        this.produtoAlimenticioUseCase = produtoAlimenticioUseCase;
        this.usuarioUseCase = usuarioUseCase;
    }

    @Override
    @Transactional
    public void salvarProdutoFichaAlimentar(List<String> produtos, int refeicao){

        boolean RefeicaoExists = false;
        RefeicaoEntity refeicaoEntity = null;
        TipoRefeicaoEnum refeicaoEnum = convertRefeicao(refeicao);
        List<Long> produtosLong = produtos.stream()
                .map(Long::parseLong)
                .collect(Collectors.toList());

        UsuarioEntity user = usuarioUseCase.buscarUsuarioLogado();

        Optional<FichaAlimentarEntity> fichasExistentesNoDia1 = fichaAlimentarRepository.findByDataAndUsuario_Id(LocalDate.now(), user.getId());

        if(fichasExistentesNoDia1.isPresent()){
            FichaAlimentarEntity fichasExistentesNoDia = fichasExistentesNoDia1.get();

            List<RefeicaoEntity> refeicoesBd = refeicaoRepository.findByFichaAlimentar_Id(fichasExistentesNoDia.getId());
            for(RefeicaoEntity refeicaoBd : refeicoesBd){
                if(refeicaoBd.getTipo() == refeicaoEnum){
                    RefeicaoExists = true;
                    refeicaoEntity = refeicaoBd;
                }
            }

            if(RefeicaoExists){
                for(Long produto : produtosLong){
                    Optional<Refeicao_ProdutoAlimenticioEntity> refeicao_produtoAlimenticioEntities = refeicao_ProdutoAlimenticioRepository.findByRefeicao_IdAndProdutoAlimenticio_Id(refeicaoEntity.getId(),produto);
                    ProdutoAlimenticioEntity produtoBd = produtoAlimenticioUseCase.buscarProdutoByIdEntity(produto);
                    //se item vazio
                    if(refeicao_produtoAlimenticioEntities.isEmpty()){
                        // refeicao existe, mas o produto não
                        Refeicao_ProdutoAlimenticioEntity refeicao_produtoAlimenticioEntity = new Refeicao_ProdutoAlimenticioEntity();
                        refeicao_produtoAlimenticioEntity.setRefeicao(refeicaoEntity);
                        RefeicaoProdutoId refeicaoProdutoId = new RefeicaoProdutoId(refeicaoEntity.getId(), produtoBd.getId());
                        refeicao_produtoAlimenticioEntity.setId(refeicaoProdutoId);
                        refeicao_produtoAlimenticioEntity.setQuantidade(1);
                        refeicao_produtoAlimenticioEntity.setProdutoAlimenticio(produtoBd);
                        refeicao_ProdutoAlimenticioRepository.save(refeicao_produtoAlimenticioEntity);

                    }else{
                        // refeicao existe, produto existe
                        Refeicao_ProdutoAlimenticioEntity refeicao_produto = refeicao_produtoAlimenticioEntities.get();
                        refeicao_produto.setQuantidade(refeicao_produto.getQuantidade()+1);
                        refeicao_ProdutoAlimenticioRepository.save(refeicao_produto);
                    }
                }
            }else{
                refeicaoEntity = new RefeicaoEntity();
                refeicaoEntity.setFichaAlimentar(fichasExistentesNoDia);
                refeicaoEntity.setTipo(refeicaoEnum);
                refeicaoRepository.save(refeicaoEntity);
                for(Long produto : produtosLong){
                    ProdutoAlimenticioEntity produtoEntity = produtoAlimenticioUseCase.buscarProdutoByIdEntity(produto);
                    RefeicaoProdutoId refeicaoProdutoId = new RefeicaoProdutoId(refeicaoEntity.getId(), produtoEntity.getId());
                    Refeicao_ProdutoAlimenticioEntity refeicao_produtoAlimenticioEntity = new Refeicao_ProdutoAlimenticioEntity();
                    refeicao_produtoAlimenticioEntity.setRefeicao(refeicaoEntity);
                    refeicao_produtoAlimenticioEntity.setId(refeicaoProdutoId);
                    refeicao_produtoAlimenticioEntity.setQuantidade(1);
                    refeicao_produtoAlimenticioEntity.setProdutoAlimenticio(produtoEntity);
                    refeicao_ProdutoAlimenticioRepository.save(refeicao_produtoAlimenticioEntity);
                }
            }

        }else{
            // ficha não existe
            FichaAlimentarEntity fichaAlimentar = new FichaAlimentarEntity();
            fichaAlimentar.setData(LocalDate.now());
            fichaAlimentar.setUsuario(user);
            fichaAlimentarRepository.save(fichaAlimentar);
            refeicaoEntity = new RefeicaoEntity();
            refeicaoEntity.setFichaAlimentar(fichaAlimentar);
            refeicaoEntity.setTipo(refeicaoEnum);
            refeicaoRepository.save(refeicaoEntity);
            for(Long produto : produtosLong){
                ProdutoAlimenticioEntity produtoEntity = produtoAlimenticioUseCase.buscarProdutoByIdEntity(produto);
                RefeicaoProdutoId refeicaoProdutoId = new RefeicaoProdutoId(refeicaoEntity.getId(), produtoEntity.getId());
                Refeicao_ProdutoAlimenticioEntity refeicao_produtoAlimenticioEntity = new Refeicao_ProdutoAlimenticioEntity();
                refeicao_produtoAlimenticioEntity.setRefeicao(refeicaoEntity);
                refeicao_produtoAlimenticioEntity.setId(refeicaoProdutoId);
                refeicao_produtoAlimenticioEntity.setQuantidade(1);
                refeicao_produtoAlimenticioEntity.setProdutoAlimenticio(produtoEntity);
                refeicao_ProdutoAlimenticioRepository.save(refeicao_produtoAlimenticioEntity);
            }
        }
    }

    @Override
    public TipoRefeicaoEnum convertRefeicao(int refeicao) {
        switch (refeicao) {
            case 1:
                return TipoRefeicaoEnum.CAFE;
            case 2:
                return TipoRefeicaoEnum.ALMOCO;
            case 3:
                return TipoRefeicaoEnum.LANCHE;
            case 4:
                return TipoRefeicaoEnum.JANTA;
            case 5:
                return TipoRefeicaoEnum.SUPLEMENTO;
            case 6:
                return TipoRefeicaoEnum.OUTRO;
            default:
                throw new IllegalArgumentException("Código de refeição inválido: " + refeicao);
        }
    }

    @Override
    public List<FichaAlimentarEntity> buscarFichaAlimentar(Long usuarioId){
        return fichaAlimentarRepository.findByUsuario_Id(usuarioId);
    }

    @Override
    public Optional<FichaAlimentarEntity> buscarFichaAlimentarDoDia(Long usuarioId, LocalDate data){
        Optional<FichaAlimentarEntity> fichaDoDiaOptional = fichaAlimentarRepository.findByDataAndUsuario_Id(data, usuarioId);
        return fichaDoDiaOptional;
    }

    @Override
    public FichaAlimentarEntity buscarFichaAlimentarMaisRecente(Long usuarioId){

    return buscarFichaAlimentar(usuarioId).stream()
            .max(Comparator.comparing(FichaAlimentarEntity::getData))
            .orElse(null);
    }

    @Override
    public List<RefeicaoEntity> buscarRefeicoesDoDia(Long usuarioId, LocalDate data){
        Optional<FichaAlimentarEntity> fichaDoDiaOptional = fichaAlimentarRepository.findByDataAndUsuario_Id(data, usuarioId);
        if(fichaDoDiaOptional.isPresent()){
            FichaAlimentarEntity fichaDoDia = fichaDoDiaOptional.get();
            List<RefeicaoEntity> refeicoesDoDia = refeicaoRepository.findByFichaAlimentar_Id(fichaDoDia.getId());
            return refeicoesDoDia;
        }
        return List.of();
    }

    @Override
    public List<Refeicao_ProdutoAlimenticioEntity> buscarProdutos_Refeicao(Long refeicaoId){
        List<Refeicao_ProdutoAlimenticioEntity> refeicao_produtoAlimenticioEntities = refeicao_ProdutoAlimenticioRepository.findByRefeicao_Id(refeicaoId);
        return refeicao_produtoAlimenticioEntities;
    }

    @Transactional
    @Override
    public String retirarProdutoRefeicao(Long id, int refeicaoRecebida){
        TipoRefeicaoEnum tipoRefeicaoEnum = null;
        List<Refeicao_ProdutoAlimenticioEntity> produtosDaRefeicao = List.of();
        switch (refeicaoRecebida){
            case 1: tipoRefeicaoEnum = TipoRefeicaoEnum.CAFE;
                break;
            case 2: tipoRefeicaoEnum = TipoRefeicaoEnum.ALMOCO;
                break;
            case 3: tipoRefeicaoEnum = TipoRefeicaoEnum.LANCHE;
                break;
            case 4: tipoRefeicaoEnum = TipoRefeicaoEnum.JANTA;
                break;
            case 5: tipoRefeicaoEnum = TipoRefeicaoEnum.SUPLEMENTO;
                break;
            case 6: tipoRefeicaoEnum = TipoRefeicaoEnum.OUTRO;
        }

        UsuarioEntity user = usuarioUseCase.buscarUsuarioLogado();
        List<RefeicaoEntity> refeicoesDodia = buscarRefeicoesDoDia(user.getId(), LocalDate.now());
        for(RefeicaoEntity refeicao : refeicoesDodia){
            if(refeicao.getTipo() == tipoRefeicaoEnum){
                 produtosDaRefeicao = buscarProdutos_Refeicao(refeicao.getId());
            }
        }

        for(Refeicao_ProdutoAlimenticioEntity produto : produtosDaRefeicao){
            if(produto.getProdutoAlimenticio().getId().equals(id)){
                refeicao_ProdutoAlimenticioRepository.delete(produto);
            }
        }

        return "produto Retirado com sucesso";
    }

    @Override
    public void adicionarRefeicaoProdutoSession(int refeicaoId, Long produtoId) {

        List<RefeicaoProdutoModel> lista =
                (List<RefeicaoProdutoModel>) session.getAttribute("itensRefeicoes");

        if (lista == null) {
            lista = new ArrayList<>();
        }

        lista.add(new RefeicaoProdutoModel(refeicaoId, produtoId));
        session.setAttribute("itensRefeicoes", lista);
    }

}
