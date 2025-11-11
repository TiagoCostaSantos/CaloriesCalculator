package com.CaloriesCalculator.database.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "FICHA_ALIMENTAR")
public class FichaAlimentarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @Column(nullable = false, length = 30)
    private LocalDate data;


    @OneToMany(mappedBy = "fichaAlimentar", cascade = CascadeType.ALL)
    private List<RefeicaoEntity> refeicoes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<RefeicaoEntity> getRefeicoes() {
        return refeicoes;
    }

    public void setRefeicoes(List<RefeicaoEntity> refeicoes) {
        this.refeicoes = refeicoes;
    }

}
