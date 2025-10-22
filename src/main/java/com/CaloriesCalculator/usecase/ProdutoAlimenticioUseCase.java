package com.CaloriesCalculator.usecase;
import com.CaloriesCalculator.model.ProdutoAlimenticioModel;

import java.util.List;

public interface ProdutoAlimenticioUseCase {

    void cadastrarProdutoAlimenticio(ProdutoAlimenticioModel produtoAlimenticioModel);

    List<ProdutoAlimenticioModel> todosProdutos();

    List<ProdutoAlimenticioModel> buscarProduto(String titulo);
}
