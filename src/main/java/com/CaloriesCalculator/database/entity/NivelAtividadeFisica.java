package com.CaloriesCalculator.database.entity;

public enum NivelAtividadeFisica {

    SEDENTARIO(1.2),
    LEVE(1.375),
    MODERADO(1.55),
    INTENSO(1.725),
    MUITO_INTENSO(1.9);

    private final double fator;

    NivelAtividadeFisica(double fator) {
        this.fator = fator;
    }

    public double getFator() {
        return fator;
    }

}
