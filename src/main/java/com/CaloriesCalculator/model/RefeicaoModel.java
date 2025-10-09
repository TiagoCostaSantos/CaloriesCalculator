package com.CaloriesCalculator.model;

import java.util.List;

public class RefeicaoModel {

    private Long id;
    private TipoRefeicaoEnum tipo; // cafe da manha, almoço, janta, lanche, outro.
    private Long fichaAlimentarId;
    private List<ProdutoAlimenticioModel> produtosAlimenticios;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoRefeicaoEnum getTipo() {
        return tipo;
    }

    public void setTipo(TipoRefeicaoEnum tipo) {
        this.tipo = tipo;
    }

    public Long getFichaAlimentarId() {
        return fichaAlimentarId;
    }

    public void setFichaAlimentarId(Long fichaAlimentarId) {
        this.fichaAlimentarId = fichaAlimentarId;
    }

    public List<ProdutoAlimenticioModel> getProdutosAlimenticios() {
        return produtosAlimenticios;
    }

    public void setProdutosAlimenticios(List<ProdutoAlimenticioModel> produtosAlimenticios) {
        this.produtosAlimenticios = produtosAlimenticios;
    }
}
