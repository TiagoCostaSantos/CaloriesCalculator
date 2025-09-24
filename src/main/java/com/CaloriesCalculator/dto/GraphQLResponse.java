package com.CaloriesCalculator.dto;

// no retorno do JSON, está seria a primeira camada o data: e essa classe aponta para a camada seguinte GetFoodByNameWrapper
public class GraphQLResponse {
    private GetFoodByNameWrapper data;

    public GetFoodByNameWrapper getData() {
        return data;
    }

    public void setData(GetFoodByNameWrapper data) {
        this.data = data;
    }
}
