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

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    public NutrientsDTO getNutrients() {
        return nutrients;
    }

    public void setNutrients(NutrientsDTO nutrients) {
        this.nutrients = nutrients;
    }
}
