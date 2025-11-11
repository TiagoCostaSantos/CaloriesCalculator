package com.CaloriesCalculator.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// para Criptografar senhas e configurar permissões
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomLoginSuccessHandler customLoginSuccessHandler;

    // TODO VERIFICAR CONFIGURAÇÕES DE ACESSO
    // TODO VERIFICAR QUANTIDADE DE VEZES QUE PODEM CHAMAR O BACK END PARA NÃO DAR SOBRECARGA EM CADA CHAMADA ESPECIFICA (TIPO CADASTRO USUARIO, EDITAR USUARIO)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // em produção deve ser configurado
                .headers().frameOptions().disable() // permite H2 rodar em iframe
                .and()
                .authorizeHttpRequests()
                    .requestMatchers(HttpMethod.POST, "/usuario/salvar","/usuario/salvarProdutosFichaAlimentar").permitAll()
                    .requestMatchers(HttpMethod.GET, "/login", "/usuario/cadastrar", "/", "/home", "/Grafico", "/produto-alimenticio/buscar-produto").permitAll()
                    .requestMatchers("/h2-console/**","/css/**", "/js/**", "/img/**", "/webjars/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login")
                    .successHandler(customLoginSuccessHandler)
                    .permitAll()
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                .and()
                .sessionManagement()
                    .invalidSessionUrl("/login?timeout")
                    .maximumSessions(1)
                    .expiredUrl("/login?expired");
        return http.build();
    }

    @Bean // ele quem diz se o usuario + senha está correto ou não
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }
}