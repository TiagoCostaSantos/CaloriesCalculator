package com.CaloriesCalculator.usecase.impl;

import com.CaloriesCalculator.database.entity.ProdutoAlimenticioEntity;
import com.CaloriesCalculator.database.repository.ProdutoAlimenticioRepository;
import com.CaloriesCalculator.model.ProdutoAlimenticioModel;
import com.CaloriesCalculator.usecase.ProdutoAlimenticioUseCase;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ProdutoAlimenticioUseCaseImpl implements ProdutoAlimenticioUseCase {

    private final ProdutoAlimenticioRepository produtoAlimenticioRepository;

    public ProdutoAlimenticioUseCaseImpl(ProdutoAlimenticioRepository produtoAlimenticioRepository){
        this.produtoAlimenticioRepository = produtoAlimenticioRepository;
    }
    @Override
    @Transactional
    public void cadastrarProdutoAlimenticio(ProdutoAlimenticioModel produtoAlimenticioModel){

        ProdutoAlimenticioEntity pe = new ProdutoAlimenticioEntity();
        pe.setTitulo(produtoAlimenticioModel.getTitulo());
        pe.setTipo(produtoAlimenticioModel.getTipo());
        pe.setKcal(produtoAlimenticioModel.getKcal());
        pe.setCarboidratos(produtoAlimenticioModel.getCarboidratos());
        pe.setProteinas(produtoAlimenticioModel.getProteinas());
        pe.setGorduraGerais(produtoAlimenticioModel.getGorduraGerais());
        pe.setDescricao(produtoAlimenticioModel.getDescricao());

        produtoAlimenticioRepository.save(pe);
    }
}
