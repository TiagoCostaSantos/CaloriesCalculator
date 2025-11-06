package com.CaloriesCalculator.usecase.impl;

import com.CaloriesCalculator.usecase.CookieUseCase;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CookieUseCaseImpl implements CookieUseCase {

    @Override
    public String formatarCookie(List<String> IdAndTipoApi, int refeicao) throws UnsupportedEncodingException {
        // Constrói a string compacta para o cookie
        List<String> listaParaCookie = IdAndTipoApi.stream()
                .map(p -> {
                    String[] partes = p.split("\\|");
                    long id = Long.parseLong(partes[0]);
                    boolean tipoApi = Boolean.parseBoolean(partes[1]);
                    return id + "," + (tipoApi ? 1 : 0) + "," + refeicao;
                })
                .toList();

        String CookieFormatado = String.join("|", listaParaCookie);
        String CookieEncodificado = URLEncoder.encode(CookieFormatado, StandardCharsets.UTF_8.toString());
        return CookieEncodificado;
    }

    @Override
    public String salvarCookie(String nomeCookie, String valor, int diasExpiracao, HttpServletResponse response) {
        Cookie cookie = new Cookie("FichaAlimentar", valor);
        cookie.setPath("/"); // acessível aonde
        cookie.setMaxAge(60 * 60 * 24 * diasExpiracao); // dura 7 dias (em segundos)
        cookie.setHttpOnly(true); // segurança: não acessível via JS
        response.addCookie(cookie);

        return "Cookie salvo com valor: " + valor;
    }

    @Override
    public String atualizarCookie(String nomeCookie, String valor, HttpServletResponse response) {
        Cookie cookie = new Cookie(nomeCookie, valor);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24 * 7); // dura 7 dias (em segundos)
        cookie.setHttpOnly(true); // segurança: não acessível via JS
        response.addCookie(cookie);

        return "Cookie Atualizado";
    }

    @Override
    public String lerCookie(String nomeCookie, HttpServletRequest request) throws UnsupportedEncodingException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for(Cookie c : cookies){
                if(nomeCookie.equals(c.getName())){
                    String CookieDecodificado = URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8.toString());
                    return CookieDecodificado;
                }
            }
        }
        return "false";
    }

    @Override
    public String removerCookie(String nomeCookie, HttpServletResponse response) {
        Cookie cookie = new Cookie(nomeCookie, null);
        cookie.setPath("/"); // mesmo path usado na criação
        cookie.setMaxAge(0); // apaga imediatamente
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        return "Cookie Apagado";
    }
}
