package com.CaloriesCalculator.database.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name="PRODUTO_ALIMENTICIO")
public class ProdutoAlimenticioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String titulo;
    @Column(nullable = false, length = 30)
    private String tipo;
    @Column(nullable = false)
    private double kcal;
    @Column(nullable = false)
    private double carboidratos;
    @Column(nullable = false)
    private double proteinas;
    @Column(nullable = false)
    private double gorduraGerais;
    @Column(nullable = false, length = 200)
    private String descricao;
    @Column(nullable = false)
    private double peso;

    @OneToMany(mappedBy = "produtoAlimenticio_id", cascade = CascadeType.ALL)
    private List<Refeicao_ProdutoAlimenticioEntity> refeicoes_ProdutosAlimenticios;

    public Long getId() {
        return id;
    }

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

    public List<Refeicao_ProdutoAlimenticioEntity> getRefeicoes_ProdutosAlimenticios() {
        return refeicoes_ProdutosAlimenticios;
    }

    public void setRefeicoes_ProdutosAlimenticios(List<Refeicao_ProdutoAlimenticioEntity> refeicoes_ProdutosAlimenticios) {
        this.refeicoes_ProdutosAlimenticios = refeicoes_ProdutosAlimenticios;
    }
}
