package com.CaloriesCalculator.dto;

import java.util.List;

// ele retornara uma lista de produtos alimenticios e tem esse nome Wrapper para refletir o nome exato da chave no json(getFoodByName)
public class GetFoodByNameWrapper {

    private List<ProdutoAlimenticioDto> getFoodByName;

    public List<ProdutoAlimenticioDto> getGetFoodByName() {
        return getFoodByName;
    }

}
