package com.CaloriesCalculator.facade;

import com.CaloriesCalculator.model.RefeicaoProdutoModel;
import com.CaloriesCalculator.usecase.CookieUseCase;
import com.CaloriesCalculator.usecase.FichaAlimentarUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
public class FichaAlimentarFacade {

    @Autowired
    private FichaAlimentarUseCase fichaAlimentarUseCase;

    @Autowired
    private CookieUseCase cookieUseCase;

    @Autowired
    private HttpSession session;

    public String salvar(List<String> produtosSelecionados, int refeicao, Principal principal, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        if(produtosSelecionados == null || produtosSelecionados.isEmpty()) {
            return "redirect:/home";
        }

        if(principal != null) {
            salvarParaUsuarioLogado(produtosSelecionados, refeicao);
        } else {
            salvarParaVisitante(produtosSelecionados, refeicao, request, response);
        }

        return "redirect:/home";
    }

    public String remover(Long id, int refeicao, Principal principal, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        if (principal != null) {
            removerParaUsuarioLogado(id, refeicao);
        } else {
            removerParaVisitante(id, refeicao, request, response);
        }

        return "redirect:/home";
    }

    private void salvarParaUsuarioLogado(List<String> produtosSelecionados, int refeicao) {
        // verificando se não possui itens na session para salvar (produtos da ultima ficha que agora com alteração devem ser salvos)
        List<RefeicaoProdutoModel> listaRefeicaoProdutosSession = (List<RefeicaoProdutoModel>) session.getAttribute("itensRefeicoes");

        if(listaRefeicaoProdutosSession != null) {
            List<Long> produtosDaRefeicao = new ArrayList<>();

            for(int i = 1; i < 6; i++){
                for (RefeicaoProdutoModel item : listaRefeicaoProdutosSession) {
                    if (item.getRefeicaoId() == i) {
                        produtosDaRefeicao.add(item.getProdutoId());
                    }
                }
                List<String> produtosDaRefeicaoString = produtosDaRefeicao.stream()
                        .map(String::valueOf)
                        .toList();
                if(!produtosDaRefeicaoString.isEmpty()){
                    fichaAlimentarUseCase.salvarProdutoFichaAlimentar(produtosDaRefeicaoString, i);
                }
                produtosDaRefeicao.clear();
            }
            session.removeAttribute("itensRefeicoes");
        }

        fichaAlimentarUseCase.salvarProdutoFichaAlimentar(produtosSelecionados, refeicao);
    }

    private void salvarParaVisitante(List<String> produtosSelecionados, int refeicao, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        String cookieAtual = cookieUseCase.lerCookie("FichaAlimentar", request);
        String novoValor = cookieUseCase.formatarCookie(produtosSelecionados, refeicao);

        if("nExists".equals(cookieAtual)) {
            cookieUseCase.salvarCookie("FichaAlimentar", novoValor, 7, response);
        } else {
            cookieUseCase.atualizarCookie("FichaAlimentar", novoValor, response, request);
        }
    }

    private void removerParaUsuarioLogado(Long id, int refeicao) {
        List<RefeicaoProdutoModel> listaRefeicaoProdutosSession  = (List<RefeicaoProdutoModel>) session.getAttribute("itensRefeicoes");

        if(listaRefeicaoProdutosSession  != null) {
            List<Long> produtosDaRefeicao = new ArrayList<>();

            for(int i = 1; i < 6; i++){
                for (RefeicaoProdutoModel item : listaRefeicaoProdutosSession) {
                    if (item.getRefeicaoId() == i) {
                        if(!(item.getRefeicaoId() == refeicao && item.getProdutoId() == id)){
                            produtosDaRefeicao.add(item.getProdutoId());
                        }
                    }
                }
                List<String> produtosDaRefeicaoString = produtosDaRefeicao.stream()
                        .map(String::valueOf)
                        .toList();

                if(!produtosDaRefeicaoString.isEmpty()){
                    fichaAlimentarUseCase.salvarProdutoFichaAlimentar(produtosDaRefeicaoString, i);
                }
                produtosDaRefeicao.clear();
            }
            session.removeAttribute("itensRefeicoes");

        } else {
            fichaAlimentarUseCase.retirarProdutoRefeicao(id, refeicao);
        }
    }

    private void removerParaVisitante(Long id, int refeicao, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        String valorCookie = cookieUseCase.lerCookie("FichaAlimentar", request);
        String valorDecodificado = URLDecoder.decode(valorCookie, StandardCharsets.UTF_8.toString());
        cookieUseCase.removerProdutoCookie(valorDecodificado, refeicao, id, response);
    }
}
