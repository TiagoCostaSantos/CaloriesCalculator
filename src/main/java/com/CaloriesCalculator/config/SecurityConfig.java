package com.CaloriesCalculator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// para Criptografar senhas
@Configuration
public class SecurityConfig {

    // TODO VERIFICAR CONFIGURAÇÕES DE ACESSO
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() // permite H2 rodar em iframe
                .and()
                .authorizeHttpRequests()
                    .requestMatchers("/h2-console/**").permitAll()
                    .anyRequest().permitAll()
                .and()
                .formLogin().disable();     // desativa o login padrão
        return http.build();
    }

    @Bean // retorna um objeto que o Spring vai gerenciar como bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


}
