package com.CaloriesCalculator.model;

import com.CaloriesCalculator.database.entity.Intensidade;
import com.CaloriesCalculator.database.entity.NivelAtividadeFisica;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class DadosUsuarioModel {

    private long id;

    @NotBlank(message = "O sexo é um campo obrigatório.")
    private String sexo;

    @NotNull(message = "A altura é obrigatória.")
    @DecimalMin(value = "50", message = "Altura inválida (mínimo 50 cm)")
    @DecimalMax(value = "300", message = "Altura inválida (máximo 300 cm)")
    private Double altura;

    @NotNull(message = "O peso é obrigatório.")
    @DecimalMin(value = "20", message = "Peso inválido (mínimo 20 kg)")
    @DecimalMax(value = "300", message = "Peso inválido (máximo 300 kg)")
    private Double peso;

    @NotNull(message = "O nível de atividade física é obrigatório.")
    @Enumerated(EnumType.STRING)
    private NivelAtividadeFisica nivelAtividadeFisica;

    @NotNull(message = "A meta de peso é obrigatória.")
    @DecimalMin(value = "20", message = "Meta inválida (mínimo 20 kg)")
    @DecimalMax(value = "300", message = "Meta inválida (máximo 300 kg)")
    private Double meta;

    @NotNull(message = "A intensidade é obrigatória.")
    @Enumerated(EnumType.STRING)
    private Intensidade intensidade;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public Double getAltura() {
        return altura;
    }

    public void setAltura(Double altura) {
        this.altura = altura;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public Double getMeta() {
        return meta;
    }

    public void setMeta(Double meta) {
        this.meta = meta;
    }

    public NivelAtividadeFisica getNivelAtividadeFisica() {
        return nivelAtividadeFisica;
    }

    public void setNivelAtividadeFisica(NivelAtividadeFisica nivelAtividadeFisica) {
        this.nivelAtividadeFisica = nivelAtividadeFisica;
    }

    public Intensidade getIntensidade() {
        return intensidade;
    }

    public void setIntensidade(Intensidade intensidade) {
        this.intensidade = intensidade;
    }
}
