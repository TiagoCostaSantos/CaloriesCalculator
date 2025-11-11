package com.CaloriesCalculator.config;

import com.CaloriesCalculator.usecase.CookieUseCase;
import com.CaloriesCalculator.usecase.FichaAlimentarUseCase;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final FichaAlimentarUseCase fichaAlimentarUseCase;
    private final CookieUseCase cookieUseCase;

    public CustomLoginSuccessHandler(FichaAlimentarUseCase fichaAlimentarUseCase, CookieUseCase cookieUseCase) {
        this.fichaAlimentarUseCase = fichaAlimentarUseCase;
        this.cookieUseCase = cookieUseCase;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        // Lê o valor do cookie salvo antes do login
        String valorCookie = cookieUseCase.lerCookie("FichaAlimentar", request);

        if (valorCookie != null && !valorCookie.isEmpty()) {
            // Salva os dados do cookie no banco de dados
            fichaAlimentarUseCase.salvarCookiesInBd(valorCookie);

            // Remove o cookie após salvar
            cookieUseCase.removerCookie("FichaAlimentar", response);
        }

        // Redireciona para a home
        response.sendRedirect("/home");
    }
}