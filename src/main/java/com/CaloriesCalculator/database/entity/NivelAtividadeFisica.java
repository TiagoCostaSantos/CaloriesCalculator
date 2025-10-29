package com.CaloriesCalculator.database.entity;

public enum NivelAtividadeFisica {

    SEDENTARIO("Sedentario"),
    LEVE("Leve"),
    MODERADO("Moderado"),
    INTENSO("Intenso"),
    MUITO_INTENSO("Muito Intenso");

    private final String descricao;

    public String getDescricao() {
        return descricao;
    }

    NivelAtividadeFisica(String descricao) {
        this.descricao = descricao;
    }
}
