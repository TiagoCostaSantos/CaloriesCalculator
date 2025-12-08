package com.CaloriesCalculator.usecase.impl;

import com.CaloriesCalculator.database.entity.FichaAlimentarEntity;
import com.CaloriesCalculator.database.entity.TipoRefeicaoEnum;
import com.CaloriesCalculator.database.entity.UsuarioEntity;
import com.CaloriesCalculator.usecase.CookieUseCase;
import com.CaloriesCalculator.usecase.FichaAlimentarUseCase;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CookieUseCaseImpl implements CookieUseCase {

    private final UsuarioUseCase usuarioUseCase;
    private final FichaAlimentarUseCase fichaAlimentarUseCase;

    public CookieUseCaseImpl(UsuarioUseCase usuarioUseCase, FichaAlimentarUseCase fichaAlimentarUseCase) {
        this.usuarioUseCase = usuarioUseCase;
        this.fichaAlimentarUseCase = fichaAlimentarUseCase;
    }

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
            List<String> conjuntoValores = new ArrayList<>();

            if (!(valorExistente.isBlank() && valorExistente == "")) {
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
        return "nExists";
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

    @Override
    public void salvarCookiesInBd(String Cookie) throws UnsupportedEncodingException {

        UsuarioEntity user = usuarioUseCase.buscarUsuarioLogado();
        List<FichaAlimentarEntity> fichas = fichaAlimentarUseCase.buscarFichaAlimentar(user.getId());

        // para salvar os cookies apenas a primeira vez
        if (fichas.isEmpty()){
            String cookieDecodificado = URLDecoder.decode(Cookie, StandardCharsets.UTF_8.toString());
            List<String> produtosIds = new ArrayList<>();

            String[] pares = cookieDecodificado.split("\\|");

            for(String par : pares){
                String[] partes = par.split(",");
                int refeicao = Integer.parseInt(partes[0]);
                String produto = partes[1];

                produtosIds.add(produto);
                fichaAlimentarUseCase.salvarProdutoFichaAlimentar(produtosIds, refeicao);
                produtosIds.clear();

            }

        }
    }

    @Override
    public List<Long> lerProdutosRefeicaoCookie(int tipoRefeicao, String nomeCookie, HttpServletRequest request) throws UnsupportedEncodingException {
        String cookie = lerCookie(nomeCookie, request);
        String CookieDecodificado = URLDecoder.decode(cookie, StandardCharsets.UTF_8.toString());

        List<Long> produtosPorRefeicao = List.of();

        switch (tipoRefeicao){
            case 0 -> {
               produtosPorRefeicao = Arrays.stream(CookieDecodificado.split("\\|"))
                        .filter(s -> s.startsWith("1"))
                        .map(s -> Long.parseLong(s.split(",")[1]))
                        .toList();
            }
            case 1 -> {
                produtosPorRefeicao = Arrays.stream(CookieDecodificado.split("\\|"))
                        .filter(s -> s.startsWith("2"))
                        .map(s -> Long.parseLong(s.split(",")[1]))
                        .toList();
            }
            case 2 -> {
                produtosPorRefeicao = Arrays.stream(CookieDecodificado.split("\\|"))
                        .filter(s -> s.startsWith("3"))
                        .map(s -> Long.parseLong(s.split(",")[1]))
                        .toList();
            }
            case 3 -> {
                produtosPorRefeicao = Arrays.stream(CookieDecodificado.split("\\|"))
                        .filter(s -> s.startsWith("4"))
                        .map(s -> Long.parseLong(s.split(",")[1]))
                        .toList();
            }
            case 4 -> {
                produtosPorRefeicao = Arrays.stream(CookieDecodificado.split("\\|"))
                        .filter(s -> s.startsWith("5"))
                        .map(s -> Long.parseLong(s.split(",")[1]))
                        .toList();
            }
            case 5 -> {
                produtosPorRefeicao = Arrays.stream(CookieDecodificado.split("\\|"))
                        .filter(s -> s.startsWith("6"))
                        .map(s -> Long.parseLong(s.split(",")[1]))
                        .toList();
            }
        }
        return produtosPorRefeicao;
    }

    @Override
    public String removerProdutoCookie(String cookieValue, int refeicao, Long id, HttpServletResponse response) throws UnsupportedEncodingException {

        String[] refeicoesCookie = cookieValue.split("\\|");
        List<String> refeicoesAtualizadas = new ArrayList<>();

        boolean removido = false;

        for (String bloco : refeicoesCookie) {
            String[] partes = bloco.split(",");
            int refeicaoAtual = Integer.parseInt(partes[0]);
            Long idProduto = Long.parseLong(partes[1]);

            // Remove apenas a primeira ocorrência
            if(!removido && refeicaoAtual == refeicao && idProduto.equals(id)){
                removido = true; // marca como removido
                continue; // pula essa única vez
            }

            refeicoesAtualizadas.add(bloco);
        }

        if (!refeicoesAtualizadas.isEmpty()) {
            String novoCookie = String.join("|", refeicoesAtualizadas);
            String cookieCodificado = URLEncoder.encode(novoCookie, StandardCharsets.UTF_8);

            Cookie cookie = new Cookie("FichaAlimentar", cookieCodificado);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24 * 7); // 7 dias
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        } else {
            removerCookie("FichaAlimentar", response);
        }

        return "Cookie atualizado";
    }
}
