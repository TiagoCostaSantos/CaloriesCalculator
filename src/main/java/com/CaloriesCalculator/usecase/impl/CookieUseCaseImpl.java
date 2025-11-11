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
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class CookieUseCaseImpl implements CookieUseCase {

    @Override
    public String formatarCookie(List<String> idProdutos, int refeicao) throws UnsupportedEncodingException {

        // Constrói a string compacta para o cookie
        List<String> listaParaCookie = idProdutos.stream()
                .map(p -> refeicao + "," + p)
                .toList();

        String CookieFormatado = String.join("|", listaParaCookie);
        String CookieEncodificado = URLEncoder.encode(CookieFormatado, StandardCharsets.UTF_8.toString());
        return CookieEncodificado;
    }

    @Override
    public String salvarCookie(String nomeCookie, String valor, int diasExpiracao, HttpServletResponse response) {
        Cookie cookie = new Cookie(nomeCookie, valor);
        cookie.setPath("/"); // acessível aonde
        cookie.setMaxAge(60 * 60 * 24 * diasExpiracao); // dura 7 dias (em segundos)
        cookie.setHttpOnly(true); // segurança: não acessível via JS
        response.addCookie(cookie);

        return "Cookie salvo com valor: " + valor;
    }

    @Override
    public String atualizarCookie(String nomeCookie, String valor, HttpServletResponse response, HttpServletRequest request) {

        try {
            // 1️⃣ Lê o cookie atual (se existir)
            Cookie[] cookies = request.getCookies();
            String valorExistente = "";
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (c.getName().equals(nomeCookie)) {
                        valorExistente = URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8.toString());
                        break;
                    }
                }
            }

            // 2️⃣ Decodifica o novo valor (ex: "1,175|3,180")
            String novoValorDecodificado = URLDecoder.decode(valor, StandardCharsets.UTF_8.toString());

            // 3️⃣ Quebra os valores em pares e remove duplicatas usando Set
            Set<String> conjuntoValores = new LinkedHashSet<>();

            if (!valorExistente.isBlank()) {
                conjuntoValores.addAll(Arrays.asList(valorExistente.split("\\|")));
            }

            if (!valor.isBlank()) {
                conjuntoValores.addAll(Arrays.asList(novoValorDecodificado.split("\\|")));
            }

            // 4️⃣ Junta novamente em formato "refeicao,produto|refeicao,produto"
            String valorFinal = String.join("|", conjuntoValores);

            // 5️⃣ Codifica e salva no cookie
            String valorFinalEncodado = URLEncoder.encode(valorFinal, StandardCharsets.UTF_8.toString());

            Cookie cookie = new Cookie(nomeCookie, valorFinalEncodado);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 7); // 7 dias
            cookie.setHttpOnly(true);
            response.addCookie(cookie);

            return "Cookie atualizado sem duplicatas";

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "Erro ao atualizar cookie: " + e.getMessage();
        }
    }

    @Override
    public String lerCookie(String nomeCookie, HttpServletRequest request) throws UnsupportedEncodingException {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for(Cookie c : cookies){
                if(nomeCookie.equals(c.getName())){
//                    String CookieDecodificado = URLDecoder.decode(c.getValue(), StandardCharsets.UTF_8.toString());
                    return c.getValue();
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
