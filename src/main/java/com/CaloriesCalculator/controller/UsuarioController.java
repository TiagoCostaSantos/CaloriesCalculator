package com.CaloriesCalculator.controller;

import com.CaloriesCalculator.database.entity.UsuarioEntity;
import com.CaloriesCalculator.model.UsuarioModel;
import com.CaloriesCalculator.usecase.CookieUseCase;
import com.CaloriesCalculator.usecase.FichaAlimentarUseCase;
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
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;
    private final FichaAlimentarUseCase fichaAlimentarUseCase;
    private final ProdutoAlimenticioUseCase produtoAlimenticioUseCase;
    private final CookieUseCase cookieUseCase;

    public UsuarioController(UsuarioUseCase usuarioUseCase,FichaAlimentarUseCase fichaAlimentarUseCase, ProdutoAlimenticioUseCase produtoAlimenticioUseCase, CookieUseCase cookieUseCase){
        this.fichaAlimentarUseCase = fichaAlimentarUseCase;
        this.produtoAlimenticioUseCase = produtoAlimenticioUseCase;
        this.cookieUseCase = cookieUseCase;
        this.usuarioUseCase = usuarioUseCase;
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
        if(principal != null) {

            fichaAlimentarUseCase.salvarProdutoFichaAlimentar(produtosSelecionados, refeicao);
        }

        String cookieEncodificado = cookieUseCase.formatarCookie(produtosSelecionados, refeicao);
        String cookieLido = cookieUseCase.lerCookie("FichaAlimentar", request);

        if("false".equals(cookieLido)){
            cookieUseCase.salvarCookie("FichaAlimentar", cookieEncodificado, 7, response);
        }else{
            String atualizado = cookieLido + "|" + cookieEncodificado;
            cookieUseCase.atualizarCookie("FichaAlimentar",atualizado,response, request);
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


