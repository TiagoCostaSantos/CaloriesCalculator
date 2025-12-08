package com.CaloriesCalculator.database.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "REFEICAO")
public class RefeicaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Anotação por ser um enum (tipo selecao)
    @Enumerated(EnumType.STRING)
    private TipoRefeicaoEnum tipo;

    @ManyToOne
    @JoinColumn(name = "fichaAlimentar_id")
    private FichaAlimentarEntity fichaAlimentar;

    // o OneToMany é apenas para conseguirmos navegar com o Java, não cria coluna nesta tabela e apenas mapeia a relacao delas
    @OneToMany(mappedBy = "refeicao", cascade = CascadeType.ALL)
    private List<Refeicao_ProdutoAlimenticioEntity> refeicoes_ProdutosAlimenticios;


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

    public FichaAlimentarEntity getFichaAlimentar() {
        return fichaAlimentar;
    }

    public void setFichaAlimentar(FichaAlimentarEntity fichaAlimentar) {
        this.fichaAlimentar = fichaAlimentar;
    }

    public List<Refeicao_ProdutoAlimenticioEntity> getRefeicoes_ProdutosAlimenticios() {
        return refeicoes_ProdutosAlimenticios;
    }

    public void setRefeicoes_ProdutosAlimenticios(List<Refeicao_ProdutoAlimenticioEntity> refeicoes_ProdutosAlimenticios) {
        this.refeicoes_ProdutosAlimenticios = refeicoes_ProdutosAlimenticios;
    }
}
