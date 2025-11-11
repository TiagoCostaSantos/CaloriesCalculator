package com.CaloriesCalculator.usecase;

import com.CaloriesCalculator.model.TipoRefeicaoEnum;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface FichaAlimentarUseCase {

    void salvarProdutoFichaAlimentar(List<String> produtos, int refeicao);

    TipoRefeicaoEnum convertRefeicao(int refeicao);

    void salvarCookiesInBd(String Cookie) throws UnsupportedEncodingException;
}
