package com.CaloriesCalculator.database.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "REFEICAO_PRODUTO_ALIMENTICIO")
public class Refeicao_ProdutoAlimenticioEntity {

    // o ID dele é embutido, ou seja é a junção dos dois campos localizados abaixo com MapsId, toda vez que tiver dois deve ser criado uma linha.
    @EmbeddedId
    private RefeicaoProdutoId id;

    @ManyToOne
    @MapsId("refeicaoId")
    @JoinColumn(name = "REFEICAO_ID")
    private RefeicaoEntity refeicao;

    @ManyToOne
    @MapsId("produtoAlimenticioId")
    @JoinColumn(name = "PRODUTO_ALIMENTICIO_ID")
    private ProdutoAlimenticioEntity produtoAlimenticio;

    @Column
    private double quantidade;

    public RefeicaoProdutoId getId() {
        return id;
    }

    public void setId(RefeicaoProdutoId id) {
        this.id = id;
    }

    public RefeicaoEntity getRefeicao() {
        return refeicao;
    }

    public void setRefeicao(RefeicaoEntity refeicao) {
        this.refeicao = refeicao;
    }

    public ProdutoAlimenticioEntity getProdutoAlimenticio() {
        return produtoAlimenticio;
    }

    public void setProdutoAlimenticio(ProdutoAlimenticioEntity produtoAlimenticio) {
        this.produtoAlimenticio = produtoAlimenticio;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }
}
