package com.CaloriesCalculator.usecase;
import com.CaloriesCalculator.database.entity.ProdutoAlimenticioEntity;
import com.CaloriesCalculator.model.ProdutoAlimenticioModel;

import java.util.List;

public interface ProdutoAlimenticioUseCase {

    void cadastrarProdutoAlimenticio(ProdutoAlimenticioModel produtoAlimenticioModel);

    List<ProdutoAlimenticioModel> todosProdutos();

    List<ProdutoAlimenticioModel> buscarProduto(String titulo);

    ProdutoAlimenticioModel buscarProdutoById(Long id);

    ProdutoAlimenticioEntity buscarProdutoByIdEntity(Long id);
}
