package com.CaloriesCalculator.config;

import com.CaloriesCalculator.service.CaptchaService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class CaptchaValidationFilter extends OncePerRequestFilter {

    @Autowired
    private CaptchaService captchaService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Só intercepta o POST do login
        if ("/login".equals(request.getServletPath())
                && request.getMethod().equalsIgnoreCase("POST")) {

            String captchaResponse = request.getParameter("g-recaptcha-response");

            if (captchaResponse == null || !captchaService.isCaptchaValid(captchaResponse)) {

                // CAPTCHA inválido — redireciona de volta com erro
                response.sendRedirect("/home?modalLogin=true&errorCaptcha");
                return; // para a cadeia de filtros (não deixa logar)
            }
        }

        filterChain.doFilter(request, response);
    }
}
