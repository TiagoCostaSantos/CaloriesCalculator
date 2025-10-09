package com.CaloriesCalculator.model;

import java.time.LocalDate;
import java.util.List;

public class FichaAlimentarModel {

    private Long id;
    private Long usuarioId;
    private LocalDate data;
    private List<RefeicaoModel> refeicoes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<RefeicaoModel> getRefeicoes() {
        return refeicoes;
    }

    public void setRefeicoes(List<RefeicaoModel> refeicoes) {
        this.refeicoes = refeicoes;
    }
}
