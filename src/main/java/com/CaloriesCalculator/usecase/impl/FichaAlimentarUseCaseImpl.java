package com.CaloriesCalculator.usecase.impl;

import com.CaloriesCalculator.database.entity.*;
import com.CaloriesCalculator.database.repository.FichaAlimentarRepository;
import com.CaloriesCalculator.database.repository.RefeicaoRepository;
import com.CaloriesCalculator.database.repository.Refeicao_ProdutoAlimenticioRepository;
import com.CaloriesCalculator.model.TipoRefeicaoEnum;
import com.CaloriesCalculator.usecase.FichaAlimentarUseCase;
import com.CaloriesCalculator.usecase.ProdutoAlimenticioUseCase;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        String email;
        if (principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
        } else {
            email = principal.toString();
        }

        // 3 dados -> Long Produtos, email, RefeicaoEnum

        // CERTEZA DE EXISTIR POIS ELE ESTÁ LOGADO
        Optional<UsuarioEntity> usuario = usuarioUseCase.buscarPorEmail(email);
        UsuarioEntity user = usuario.get();

        List<FichaAlimentarEntity> fichasExistentesNoDia = fichaAlimentarRepository.findByDataAndUsuario_Id(LocalDate.now(), user.getId());

        if(!fichasExistentesNoDia.isEmpty()){
            for(FichaAlimentarEntity ficha : fichasExistentesNoDia){
                List<RefeicaoEntity> refeicoesBd = refeicaoRepository.findByFichaAlimentar_Id(ficha.getId());
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
                    refeicaoEntity.setFichaAlimentar(ficha);
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
        }else{
            // ficha não existe
            FichaAlimentarEntity fichaAlimentar = new FichaAlimentarEntity();
            fichaAlimentar.setData(LocalDate.now());
            fichaAlimentar.setUsuario(usuario.get());
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
    public void salvarCookiesInBd(String Cookie) throws UnsupportedEncodingException {
        System.out.println(Cookie);
        String cookieDecodificado = URLDecoder.decode(Cookie, StandardCharsets.UTF_8.toString());
        System.out.println(cookieDecodificado);
        List<String> produtosIds = new ArrayList<>();
        List<Integer> refeicoes = new ArrayList<>();

        String[] pares = cookieDecodificado.split("\\|");

        for(String par : pares){
            String[] partes = par.split(",");
            int refeicao = Integer.parseInt(partes[0]);
            String produto = partes[1];

            refeicoes.add(refeicao);
            produtosIds.add(produto);
        }

        for(int refeicao : refeicoes){
            salvarProdutoFichaAlimentar(produtosIds, refeicao);
        }
    }
}
