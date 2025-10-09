package com.CaloriesCalculator.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

// implementa o Serializable que é uma interface do Java que permite que o objeto se torne uma sequencia de bytes, e o JPA preciso disso para armzenar e comparar esse objeto internamente
@Embeddable
public class RefeicaoProdutoId implements Serializable {

    @Column(name = "REFEICAO_ID")
    private Long refeicaoId;

    @Column(name = "PRODUTO_ALIMENTICIO_ID")
    private Long produtoAlimenticioId;

    // Construtor usado pelo JPA
    public RefeicaoProdutoId() {}

    // Construtor usado pelo nosso codigo passando os parametros
    public RefeicaoProdutoId(Long refeicaoId, Long produtoAlimenticioId) {
        this.refeicaoId = refeicaoId;
        this.produtoAlimenticioId = produtoAlimenticioId;
    }

    @Override // estamos substituindo o metodo equals herdado de object(Object é a classe pai de todas as classes Java - define varios metodos funadamentais das classes )
    public boolean equals(Object o) {
        // se compararmos a mesma instancia  a.equals.(a)
        if (this == o) return true;
        // verifica se o Object o foi instanciado diferente de null, e se ele é da mesma classe do objeto que o chamou (RefeicaoProdutoId)
        if (o == null || getClass() != o.getClass()) return false;

        // se continuou sabemos o seguinte -> estamos comparando duas instancias diferentes na memoria, e o objeto passado não é nulo  (foi instanciado), e é duas classes do mesmo tipo (RefeicaoProdutoId)
        // Aqui fazemos um Cast -> o objeto "o" é um Objeto qualquer pois nesse metodo se recebe um objeto mas como sabemos que ele é exatamente um RefeicaoProdutoId precisamos muda-lo para este tipo e por isso a linha abaixo
        RefeicaoProdutoId that = (RefeicaoProdutoId) o;
        return Objects.equals(refeicaoId, that.refeicaoId) &&
                Objects.equals(produtoAlimenticioId, that.produtoAlimenticioId);
    }

    // Gera um codigo hash do conjunto dos dois atributos passado, ou seja, ele gera uma impressao digital que sempre sera a mesma se compararmos com dois valores iguais (refeicaoId e ProdutoId), independente da instancia
    @Override
    public int hashCode() {
        return Objects.hash(refeicaoId, produtoAlimenticioId);
    }

    public Long getRefeicaoId() {
        return refeicaoId;
    }

    public void setRefeicaoId(Long refeicaoId) {
        this.refeicaoId = refeicaoId;
    }

    public Long getProdutoAlimenticioId() {
        return produtoAlimenticioId;
    }

    public void setProdutoAlimenticioId(Long produtoAlimenticioId) {
        this.produtoAlimenticioId = produtoAlimenticioId;
    }
}
