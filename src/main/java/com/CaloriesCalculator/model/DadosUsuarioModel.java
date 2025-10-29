package com.CaloriesCalculator.model;

import com.CaloriesCalculator.database.entity.MotivoEnum;
import com.CaloriesCalculator.database.entity.NivelAtividadeFisica;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

public class DadosUsuarioModel {

    private long id;

    @NotBlank(message = "O sexo é um campo obrigatório.")
    private String sexo;

    @NotBlank(message = "A altura é um campo obrigatório")
    @DecimalMin(value = "0.5", message = "Altura invalida")
    @DecimalMax(value = "3.0", message = "Altura invalida")
    private Double altura;

    @NotBlank(message = "O peso é um campo obrigatório")
    @DecimalMin(value = "20.0", message = "Peso invalido")
    @DecimalMax(value = "300.0", message = "Peso invalido")
    private Double peso;

    @NotBlank(message = "O nivel de atividade Fisica é obrigatório")
    @Enumerated(EnumType.STRING)
    private NivelAtividadeFisica nivelAtividadeFisica;

    @NotBlank(message = "O Motivo é um campo obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private MotivoEnum motivoEnum;

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

    public NivelAtividadeFisica getNivelAtividadeFisica() {
        return nivelAtividadeFisica;
    }

    public void setNivelAtividadeFisica(NivelAtividadeFisica nivelAtividadeFisica) {
        this.nivelAtividadeFisica = nivelAtividadeFisica;
    }

    public MotivoEnum getMotivoEnum() {
        return motivoEnum;
    }

    public void setMotivoEnum(MotivoEnum motivoEnum) {
        this.motivoEnum = motivoEnum;
    }
}
