package com.CaloriesCalculator.controller;

import com.CaloriesCalculator.database.entity.UsuarioEntity;
import com.CaloriesCalculator.model.DadosUsuarioModel;
import com.CaloriesCalculator.model.RefeicaoProdutoModel;
import com.CaloriesCalculator.model.UsuarioModel;
import com.CaloriesCalculator.service.CaptchaService;
import com.CaloriesCalculator.usecase.CookieUseCase;
import com.CaloriesCalculator.usecase.FichaAlimentarUseCase;
import com.CaloriesCalculator.usecase.ProdutoAlimenticioUseCase;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.sql.Ref;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private HttpSession session;

    private final UsuarioUseCase usuarioUseCase;
    private final FichaAlimentarUseCase fichaAlimentarUseCase;
    private final ProdutoAlimenticioUseCase produtoAlimenticioUseCase;
    private final CookieUseCase cookieUseCase;
    private final CaptchaService captchaService;

    public UsuarioController(UsuarioUseCase usuarioUseCase,FichaAlimentarUseCase fichaAlimentarUseCase, CaptchaService captchaService, ProdutoAlimenticioUseCase produtoAlimenticioUseCase, CookieUseCase cookieUseCase){
        this.fichaAlimentarUseCase = fichaAlimentarUseCase;
        this.produtoAlimenticioUseCase = produtoAlimenticioUseCase;
        this.cookieUseCase = cookieUseCase;
        this.usuarioUseCase = usuarioUseCase;
        this.captchaService = captchaService;
    }

    @GetMapping("/cadastrar")
    public String FormCadastro(Principal principal, Model model){

        if(principal != null){
            // se tentarem entrar nesta pagina, logada, faz o logout e manda pro login
            return "redirect:/logout?redirectTo=login";
        }
        model.addAttribute("usuario", new UsuarioModel());
        return "cadastroUsuario";
    } // todo verificar se ainda vou usar isso

    @PostMapping("/salvar")
    public String SalvarCadastroUsuario(@Valid @ModelAttribute("usuario") UsuarioModel usuarioModel, @RequestParam(value = "g-recaptcha-response", required = false) String captchaResponse, BindingResult result, @RequestParam("origem") String origem,RedirectAttributes redirectAttributes, Principal principal, Model model){

        if(result.hasErrors()){
            if("cadastro".equals(origem)){
                // se for erros do modal de cadastro
                redirectAttributes.addFlashAttribute("abrirModalCadastro", true);
                if(result.hasErrors()){
                    redirectAttributes.addFlashAttribute("erros", result.getAllErrors());
                }
                return "redirect:/home";
            }else{
                // se for erros da pagina de atualizar perfil
                model.addAttribute("erros", result.getAllErrors());
                return "meuPerfil";
            }
        }

        if(principal == null){
            // não logado
            boolean captchaValido = captchaService.isCaptchaValid(captchaResponse);

            if (!captchaValido) {
                redirectAttributes.addFlashAttribute("abrirModalCadastro", true);
                return "redirect:/home?errorCaptcha";
            }

            if(usuarioUseCase.cadastrarUsuario(usuarioModel)){
                return "redirect:/home?modalLogin=true&cadastrado"; // todo redirecionar para modal de login novo
            }else{
                // Usuario já existe
                redirectAttributes.addFlashAttribute("abrirModalCadastro", true);
                redirectAttributes.addFlashAttribute("emailExists", true);
                return "redirect:/home";
            }
        }else{
            //Usuario logado
            usuarioUseCase.atualizarUsuario(usuarioModel, principal.getName());
            return "redirect:/meuPerfil?dadosAtualizados";
        }

    }

    @PostMapping("/salvarProdutosFichaAlimentar")
    public String SalvarFichaAlimentar(@RequestParam(required = false) List<String> produtosSelecionados, @RequestParam int refeicao, Principal principal, HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {

        //se nenhum produto selecionado voltar pra home
        if(produtosSelecionados == null || produtosSelecionados.isEmpty()){
            return "redirect:/home";
        }

        // se logado
        if(principal != null) {
            // verificando se não possui itens na session para salvar (produtos da ultima ficha que agora com alteração devem ser salvos)
            List<RefeicaoProdutoModel> listaRefeicaoProdutosSession = (List<RefeicaoProdutoModel>) session.getAttribute("itensRefeicoes");
            if(listaRefeicaoProdutosSession != null){

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
        }else{
            String cookieEncodificado = cookieUseCase.formatarCookie(produtosSelecionados, refeicao);
            String cookieLido = cookieUseCase.lerCookie("FichaAlimentar", request);

            if("nExists".equals(cookieLido)){
                cookieUseCase.salvarCookie("FichaAlimentar", cookieEncodificado, 7, response);
            }else{
                cookieUseCase.atualizarCookie("FichaAlimentar",cookieEncodificado,response, request);
            }
        }

        return "redirect:/home";
    }

    @PostMapping("/removerProdutosFichaAlimentar")
    public String removerProdutoFichaAlimentar(@RequestParam Long id, @RequestParam int refeicao, Principal principal, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        if(principal != null){
            List<RefeicaoProdutoModel> listaRefeicaoProdutosSession = (List<RefeicaoProdutoModel>) session.getAttribute("itensRefeicoes");
            if(listaRefeicaoProdutosSession != null){

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
            }else{
                fichaAlimentarUseCase.retirarProdutoRefeicao(id, refeicao);
            }
        }else{
            String valorCookie = cookieUseCase.lerCookie("FichaAlimentar", request);
            String valorDecodificado = URLDecoder.decode(valorCookie, StandardCharsets.UTF_8.toString());
            cookieUseCase.removerProdutoCookie(valorDecodificado, refeicao, id, response);
        }
        return "redirect:/home";
    }

    @GetMapping("/formSalvarDados")
    public String mostrarPagSalvarDados(Model model){
        model.addAttribute("dadosUsuarioModel", new DadosUsuarioModel());
        return "fragments/dadosUsuario :: conteudo";
    }

    @PostMapping("/salvarDados")
    public String salvarDados(@Valid @ModelAttribute DadosUsuarioModel dadosUsuarioModel, BindingResult result, Model model, RedirectAttributes redirectAttributes, HttpServletResponse response){

        // Verificando tratamento de caracteres e campos
        if(result.hasErrors()){
            redirectAttributes.addFlashAttribute("erros", result.getAllErrors());
            redirectAttributes.addFlashAttribute("AcionarModalDados", true);
            redirectAttributes.addFlashAttribute("dadosUsuarioModel", dadosUsuarioModel);
            return "redirect:/home"; // ao entrar na home verificar se não houve erros para barrar logo e não fazer as contas tudo denovo ou mostrar verificar como fazer
            // TODO COLOCAR TRATAMENTO DE FRONT END

        }

        usuarioUseCase.salvarDadosUsuario(dadosUsuarioModel);

        redirectAttributes.addFlashAttribute("fecharModalDados", true);
        // TODO NÃO CRIAR SE JA TIVER
        // TODO QUANDO SALVA ELE NÃO FECHA O MODAL, E PRECISA VERIFICAR PARA NÃO SALVAR COM DUPLICIDADE
        return "redirect:/home";
    }

}


