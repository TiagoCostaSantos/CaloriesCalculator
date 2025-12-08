package com.CaloriesCalculator.usecase.impl;

import com.CaloriesCalculator.database.entity.ProdutoAlimenticioEntity;
import com.CaloriesCalculator.database.repository.ProdutoAlimenticioRepository;
import com.CaloriesCalculator.model.ProdutoAlimenticioModel;
import com.CaloriesCalculator.usecase.ProdutoAlimenticioUseCase;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public List<ProdutoAlimenticioModel> todosProdutos(){
        List<ProdutoAlimenticioEntity> entities = produtoAlimenticioRepository.findAll();
        return entities.stream().map(this :: entityToModel).collect(Collectors.toList());
    }

    @Override
    public ProdutoAlimenticioEntity buscarProdutoByIdEntity(Long id){
        Optional<ProdutoAlimenticioEntity> produto = produtoAlimenticioRepository.findById(id);
        return produto.get();
    }

    @Override
    public ProdutoAlimenticioModel buscarProdutoById(Long id){
        Optional<ProdutoAlimenticioEntity> produto = produtoAlimenticioRepository.findById(id);
        return entityToModel(produto.get());
    }

    @Override
    public List<ProdutoAlimenticioModel> buscarProduto(String titulo) {
        List<ProdutoAlimenticioEntity> entities = produtoAlimenticioRepository.findByTituloContainingIgnoreCase(titulo);
        return entities.stream().map(this :: entityToModel).collect(Collectors.toList());
    }

    public ProdutoAlimenticioModel entityToModel(ProdutoAlimenticioEntity produtoAlimenticioEntity){
        ProdutoAlimenticioModel model = new ProdutoAlimenticioModel();
        model.setId(produtoAlimenticioEntity.getId());
        model.setTitulo(produtoAlimenticioEntity.getTitulo());
        model.setTipo(produtoAlimenticioEntity.getTipo());
        model.setKcal(produtoAlimenticioEntity.getKcal());
        model.setCarboidratos(produtoAlimenticioEntity.getCarboidratos());
        model.setProteinas(produtoAlimenticioEntity.getProteinas());
        model.setGorduraGerais(produtoAlimenticioEntity.getGorduraGerais());
        model.setDescricao(produtoAlimenticioEntity.getDescricao());
        model.setPeso(produtoAlimenticioEntity.getPeso());

        return model;
    }
}
