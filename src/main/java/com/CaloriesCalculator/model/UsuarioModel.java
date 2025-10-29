package com.CaloriesCalculator.model;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

public class UsuarioModel {

    @NotBlank(message = "campo obrigatorio")
    @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "O nome deve conter apenas letras e espaços")
    @Size(min = 2, max = 60, message = "O nome deve ter entre 2 e 60 caracteres")
    private String nome;

    @NotBlank(message = "campo é obrigatório")
    @Pattern(regexp = "^[A-Za-zÀ-ÿ\\s]+$", message = "O sobrenome deve conter apenas letras e espaços")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String sobrenome;

    @NotBlank(message = "A Senha é obrigatoria")
    @Size(min = 5, max = 40, message = "A senha deve possuir entre 5 a 30 caracteres")
    private String password;

    @NotBlank(message = "email é obrigatório.")
    @Email(message = "Informe um e-mail válido.")
    @Size(max = 100, message = "O e-mail pode ter no máximo 100 caracteres.")
    private String email;

    @NotNull(message = "A data de nascimento é obrigatória.")
    @Past(message = "A data de nascimento deve ser anterior à data atual.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataNascimento;

    @AssertTrue(message = "A data de nascimento deve ser posterior a 01/01/1900.")
    public boolean isDataNascimentoValida() {
        if (dataNascimento == null) return true;
        LocalDate dataMinima = LocalDate.of(1900, 1, 1);
        return !dataNascimento.isBefore(dataMinima);
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

}
