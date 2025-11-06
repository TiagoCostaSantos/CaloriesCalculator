package com.CaloriesCalculator.controller;

import com.CaloriesCalculator.client.TacoGraphQLApiClient;
import com.CaloriesCalculator.model.ProdutoAlimenticioModel;
import com.CaloriesCalculator.model.UsuarioModel;
import com.CaloriesCalculator.usecase.CookieUseCase;
import com.CaloriesCalculator.usecase.ProdutoAlimenticioUseCase;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;
    private final TacoGraphQLApiClient tacoGraphQLApiClient;
    private final ProdutoAlimenticioUseCase produtoAlimenticioUseCase;
    private final CookieUseCase cookieUseCase;

    public UsuarioController(UsuarioUseCase usuarioUseCase, TacoGraphQLApiClient tacoGraphQLApiClient, ProdutoAlimenticioUseCase produtoAlimenticioUseCase, CookieUseCase cookieUseCase){
        this.usuarioUseCase = usuarioUseCase;
        this.tacoGraphQLApiClient = tacoGraphQLApiClient;
        this.produtoAlimenticioUseCase = produtoAlimenticioUseCase;
        this.cookieUseCase = cookieUseCase;
    }

    @GetMapping("/cadastrar")
    public String FormCadastro(Principal principal, Model model){

        if(principal != null){
            // se tentarem entrar nesta pagina, logada, faz o logout e manda pro login
            return "redirect:/logout?redirectTo=login";
        }
        model.addAttribute("usuario", new UsuarioModel());
        return "cadastroUsuario";
    }

    @PostMapping("/salvar")
    public String SalvarCadastroUsuario(@Valid @ModelAttribute("usuario") UsuarioModel usuarioModel, BindingResult result, @RequestParam("origem") String origem, Principal principal, Model model){

            // Verificando tratamento de caracteres e campos
            if(result.hasErrors()){
                model.addAttribute("erros", result.getAllErrors());
                if ("cadastro".equals(origem)) {
                    return "cadastroUsuario";
                }else{
                    return "meuPerfil";
                }
            }
            if(principal == null){
                // não logado
                if(usuarioUseCase.cadastrarUsuario(usuarioModel)){
                    return "redirect:/login?cadastrado";
                }
                return "redirect:/usuario/cadastrar?emailExists";
            }
            usuarioUseCase.atualizarUsuario(usuarioModel, principal.getName());
            return "redirect:/meuPerfil?dadosAtualizados";
    }


    @PostMapping("/salvarProdutosFichaAlimentar")
    public String SalvarFichaAlimentar(@RequestParam(required = false) List<String> produtosSelecionados, @RequestParam int refeicao, Principal principal, HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {

        //se nenhum produto selecionado voltar pra home
        if(produtosSelecionados == null || produtosSelecionados.isEmpty()){
            return "redirect:/home";
        }

        // se logado
        if(principal != null){
            usuarioUseCase.salvarProdutoFichaAlimentar("tiagocosta7603@gmail.com", produtosSelecionados, refeicao);
        }

        // Cookies salvando e atualizando se não logado.
        String CookieFormatado = cookieUseCase.formatarCookie(produtosSelecionados, refeicao);
        if(cookieUseCase.lerCookie("ProdutosFichaAlimentar", request) == "false"){
            cookieUseCase.salvarCookie("ProdutosFichaAlimentar", CookieFormatado, 7, response);
        }
        else{
            cookieUseCase.atualizarCookie("ProdutosFichaAlimentar", cookieUseCase.lerCookie("ProdutosFichaAlimentar", request) + CookieFormatado,response);
        }


        // pesquisar model completo dos itens selecionados
//        List<ProdutoAlimenticioModel> produtosSelecionadosModel = new ArrayList<>();
//        for(String p : produtosSelecionados){
//            String[] partes = p.split(",");
//            Long id = Long.parseLong(partes[0]);
//            boolean tipoApi = Boolean.parseBoolean(partes[1]);
//
//            if(tipoApi){
//                produtosSelecionadosModel.add(tacoGraphQLApiClient.buscarProdutoApiId(id));
//            }else{
//                produtosSelecionadosModel.add(produtoAlimenticioUseCase.buscarProdutoById(id));
//            }
//        }

        return "redirect:/home";
    }

}


