package com.CaloriesCalculator.usecase;

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

}

