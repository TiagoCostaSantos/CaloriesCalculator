package com.CaloriesCalculator.config;

import com.CaloriesCalculator.database.entity.FichaAlimentarEntity;
import com.CaloriesCalculator.database.entity.UsuarioEntity;
import com.CaloriesCalculator.usecase.CookieUseCase;
import com.CaloriesCalculator.usecase.FichaAlimentarUseCase;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDate;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final CookieUseCase cookieUseCase;
    private final UsuarioUseCase usuarioUseCase;

    public CustomLoginSuccessHandler(CookieUseCase cookieUseCase, UsuarioUseCase usuarioUseCase) {
        this.cookieUseCase = cookieUseCase;
        this.usuarioUseCase = usuarioUseCase;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // Lê o valor do cookie salvo antes do login
        String valorCookie = cookieUseCase.lerCookie("FichaAlimentar", request);
        if (valorCookie != null && !valorCookie.isEmpty() && !valorCookie.equals("nExists")) {
            // Salva os dados do cookie no banco de dados
            cookieUseCase.salvarCookiesInBd(valorCookie);
            // Remove o cookie após salvar
            cookieUseCase.removerCookie("FichaAlimentar", response);
        }

        UsuarioEntity user = usuarioUseCase.buscarUsuarioLogado();
        // Redireciona para a home
        response.sendRedirect("/home?abrirModal=true");
    }
}