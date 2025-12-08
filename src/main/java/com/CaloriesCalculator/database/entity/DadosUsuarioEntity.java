package com.CaloriesCalculator.database.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "DADOS_USUARIO")
public class DadosUsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private Double altura;

    @Column(nullable = false, length = 20)
    private Double peso;

    @Column(nullable = false, length = 5)
    private String sexo;

    @Column(nullable = false, length = 30)
    private LocalDate dataCadastroDados;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NivelAtividadeFisica nivelAtividadeFisica;

    @Column(nullable = false, length = 30)
    private Double metaPeso;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Intensidade intensidade;

    @OneToOne
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public LocalDate getDataCadastroDados() {
        return dataCadastroDados;
    }

    public void setDataCadastroDados(LocalDate dataCadastroDados) {
        this.dataCadastroDados = dataCadastroDados;
    }

    public NivelAtividadeFisica getNivelAtividadeFisica() {
        return nivelAtividadeFisica;
    }

    public void setNivelAtividadeFisica(NivelAtividadeFisica nivelAtividadeFisica) {
        this.nivelAtividadeFisica = nivelAtividadeFisica;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public Double getMetaPeso() {
        return metaPeso;
    }

    public void setMetaPeso(Double metaPeso) {
        this.metaPeso = metaPeso;
    }

    public Intensidade getIntensidade() {
        return intensidade;
    }

    public void setIntensidade(Intensidade intensidade) {
        this.intensidade = intensidade;
    }
}
