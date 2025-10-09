package com.CaloriesCalculator.database.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="USUARIO")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nome;
    @Column(length = 200)
    private String sobrenome;
    @Column(nullable = false, length = 200)
    private String password;
    @Column(unique = true,nullable = false, length = 200)
    private String email;
    @Column(nullable = false, length = 30)
    private LocalDate dataNascimento;
    @Column(nullable = false, length = 30)
    private LocalDate dataCadastro;
    @Column(nullable = false, length = 20)
    private Double altura;
    @Column(nullable = false, length = 20)
    private Double peso;


    //Cascade define que todas as ações feitas no usuario, cascateiam para as fichas alimentares (apagar, criar)
    @OneToMany(mappedBy = "usuario_id", cascade = CascadeType.ALL)
    private List<FichaAlimentarEntity> fichasAlimentares;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
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

    public List<FichaAlimentarEntity> getFichasAlimentares() {
        return fichasAlimentares;
    }

    public void setFichasAlimentares(List<FichaAlimentarEntity> fichasAlimentares) {
        this.fichasAlimentares = fichasAlimentares;
    }


}
