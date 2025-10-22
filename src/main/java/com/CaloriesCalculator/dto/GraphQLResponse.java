package com.CaloriesCalculator.dto;

// no retorno do JSON, está seria a primeira camada o data: e essa classe aponta para a camada seguinte GetFoodByNameWrapper ou GetAllFoodWrapper
//Aqui passamos um parametro Generico ou seja, utilizei a letra T, para isso, o atributo data pode ser de qualquer tipo GetFoodByName ou GetAllFood;
public class GraphQLResponse<T> {

    // private GetFoodByNameWrapper data;
    private T data;

    public T getData() {
        return data;
    }

}
