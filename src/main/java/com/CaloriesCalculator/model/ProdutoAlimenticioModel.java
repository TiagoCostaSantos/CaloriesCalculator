package com.CaloriesCalculator.model;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

public class ProdutoAlimenticioModel {

    @Min(value = 0, message = "O valor minimo permitido é 0")
    @Max(value = 3000, message = "O valor maximo permitido é 3000")
    private Long id;

    @NotBlank(message = "O Titulo é obrigatório")
    @Size(min = 2, max = 130, message = "Titulo invalido")
    private String titulo;

    @NotBlank(message = "O Tipo é obrigatório")
    @Size(min = 2, max = 130, message = "Tipo invalido")
    @Column(nullable = false, length = 30)
    private String tipo;

    @NotNull(message = "As Kcal é obrigatoria")
    @Min(value = 0, message = "O valor minimo permitido é 0")
    @Max(value = 3000, message = "O valor maximo permitido é 3000")
    private double kcal;

    @NotNull(message = "Os Carboidratos é obrigatorio")
    @Min(value = 0, message = "O valor minimo permitido é 0")
    @Max(value = 3000, message = "O valor maximo permitido é 3000")
    private double carboidratos;

    @NotNull(message = "As proteinas é obrigatoria")
    @Min(value = 0, message = "O valor minimo permitido é 0")
    @Max(value = 3000, message = "O valor maximo permitido é 3000")
    private double proteinas;

    // @NOTNUL
    @Min(value = 0, message = "O valor minimo permitido é 0")
    @Max(value = 3000, message = "O valor maximo permitido é 3000")
    private double gorduraGerais; // não possue na api

    @Size(min = 0, max = 200, message = "Mensagem invalida")
    private String descricao;

    @Min(value = 0, message = "O valor minimo permitido é 0")
    @Max(value = 3000, message = "O valor maximo permitido é 3000")
    private double peso; // não possue na api

    private boolean tipoApi = false;

    private boolean selecionado = false;

    public boolean getSelecionado() {
        return selecionado;
    }

    public void setSelecionado(boolean selecionado) {
        this.selecionado = selecionado;
    }

    public boolean getTipoApi() {
        return tipoApi;
    }

    public void setTipoApi(boolean tipoApi) {
        this.tipoApi = tipoApi;
    }

    public Long getId() {return id;}

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getKcal() {
        return kcal;
    }

    public void setKcal(double kcal) {
        this.kcal = kcal;
    }

    public double getCarboidratos() {
        return carboidratos;
    }

    public void setCarboidratos(double carboidratos) {
        this.carboidratos = carboidratos;
    }

    public double getProteinas() {
        return proteinas;
    }

    public void setProteinas(double proteinas) {
        this.proteinas = proteinas;
    }

    public double getGorduraGerais() {
        return gorduraGerais;
    }

    public void setGorduraGerais(double gorduraGerais) {
        this.gorduraGerais = gorduraGerais;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
}
