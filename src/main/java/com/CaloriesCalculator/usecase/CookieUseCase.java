package com.CaloriesCalculator.usecase;

import com.CaloriesCalculator.database.entity.TipoRefeicaoEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface CookieUseCase {

    String formatarCookie(List<String> idProdutos, int refeicao) throws UnsupportedEncodingException;

    String salvarCookie(String nomeCookie, String valor, int diasExpiracao, HttpServletResponse response);

    String atualizarCookie(String nomeCookie, String valor, HttpServletResponse response, HttpServletRequest request);

    String lerCookie(String nomeCokie, HttpServletRequest request) throws UnsupportedEncodingException;

    String removerCookie(String nomeCookie, HttpServletResponse response);

    void salvarCookiesInBd(String Cookie) throws UnsupportedEncodingException;

    List<Long> lerProdutosRefeicaoCookie(int tipoRefeicao, String nomeCookie, HttpServletRequest request) throws UnsupportedEncodingException;

    String removerProdutoCookie(String cookieValue, int refeicao, Long id, HttpServletResponse response) throws UnsupportedEncodingException;
}

