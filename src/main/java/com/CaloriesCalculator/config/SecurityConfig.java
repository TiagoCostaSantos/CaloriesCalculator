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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

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

    @Autowired
    private CaptchaValidationFilter captchaFilter;

    // TODO VERIFICAR CONFIGURAÇÕES DE ACESSO
    // TODO VERIFICAR QUANTIDADE DE VEZES QUE PODEM CHAMAR O BACK END PARA NÃO DAR SOBRECARGA EM CADA CHAMADA ESPECIFICA (TIPO CADASTRO USUARIO, EDITAR USUARIO)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // Handler personalizado para erro no login
        SimpleUrlAuthenticationFailureHandler failureHandler =
                new SimpleUrlAuthenticationFailureHandler("/home?modalLogin=true&error=true");

        http
                .csrf().disable() // em produção deve ser configurado
                .headers().frameOptions().disable() // permite H2 rodar em iframe
                .and()
                .authorizeHttpRequests()
                    .requestMatchers(HttpMethod.POST, "/usuario/salvar","/usuario/salvarProdutosFichaAlimentar", "/usuario/removerProdutosFichaAlimentar").permitAll()
                    .requestMatchers(HttpMethod.GET, "/login", "/usuario/cadastrar", "/", "/home", "/Grafico", "/produto-alimenticio/buscar-produto").permitAll()
                    .requestMatchers("/h2-console/**","/css/**", "/js/**", "/img/**", "/webjars/**").permitAll()
                    .anyRequest().authenticated()
                .and()
                .addFilterBefore(captchaFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                    .loginPage("/home")
                    .loginProcessingUrl("/login")
                    .failureHandler(failureHandler)
                    .successHandler(customLoginSuccessHandler)
                    .permitAll()
                .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/home?modalLogin=true&logout")
                    .invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
                    .permitAll()
                .and()
                .sessionManagement()
                    .invalidSessionUrl("/home?modalLogin=true&timeout")
                    .maximumSessions(1)
                    .expiredUrl("/home?modalLogin=true&expired");
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