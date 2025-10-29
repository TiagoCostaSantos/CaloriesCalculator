package com.CaloriesCalculator.database.entity;

public enum MotivoEnum {

    PERDADEPESO("Perda de peso"),
    AUMENTODEPESO("Aumento de peso");

    private String descricao;

    MotivoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
