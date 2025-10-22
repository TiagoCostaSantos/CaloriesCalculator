package com.CaloriesCalculator.dto;

// representa cada alimento individual dentro da lista
public class ProdutoAlimenticioDto {

    private Integer id;
    private String name;
    private CategoryDTO category;
    private NutrientsDTO nutrients;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public NutrientsDTO getNutrients() {
        return nutrients;
    }

}
