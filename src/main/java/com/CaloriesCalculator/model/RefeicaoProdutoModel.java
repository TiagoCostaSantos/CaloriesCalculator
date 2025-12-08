package com.CaloriesCalculator.model;

public class RefeicaoProdutoModel {

    private int refeicaoId;
    private Long produtoId;

    public RefeicaoProdutoModel(int refeicaoId, Long produtoId) {
        this.refeicaoId = refeicaoId;
        this.produtoId = produtoId;
    }

    public int getRefeicaoId() { return refeicaoId; }
    public Long getProdutoId() { return produtoId; }
}
