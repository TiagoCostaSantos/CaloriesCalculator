package com.CaloriesCalculator.controller;

import com.CaloriesCalculator.database.entity.UsuarioEntity;
import com.CaloriesCalculator.facade.FichaAlimentarFacade;
import com.CaloriesCalculator.facade.UsuarioFacade;
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
    private final UsuarioFacade usuarioFacade;
    private final FichaAlimentarFacade fichaAlimentarFacade;

    public UsuarioController(UsuarioUseCase usuarioUseCase, FichaAlimentarUseCase fichaAlimentarUseCase, CaptchaService captchaService, ProdutoAlimenticioUseCase produtoAlimenticioUseCase, CookieUseCase cookieUseCase, UsuarioFacade usuarioFacade, FichaAlimentarFacade fichaAlimentarFacade){
        this.fichaAlimentarUseCase = fichaAlimentarUseCase;
        this.produtoAlimenticioUseCase = produtoAlimenticioUseCase;
        this.cookieUseCase = cookieUseCase;
        this.usuarioUseCase = usuarioUseCase;
        this.captchaService = captchaService;
        this.usuarioFacade = usuarioFacade;
        this.fichaAlimentarFacade = fichaAlimentarFacade;
    }

    @PostMapping("/salvar")
    public String SalvarCadastroUsuario(@Valid @ModelAttribute("usuario") UsuarioModel usuarioModel, BindingResult result, @RequestParam(value = "g-recaptcha-response", required = false) String captchaResponse, @RequestParam("origem") String origem,RedirectAttributes redirectAttributes, Principal principal, Model model){
        return usuarioFacade.salvar(usuarioModel, result, captchaResponse, origem, principal, redirectAttributes, model);
    }

    @PostMapping("/salvarProdutosFichaAlimentar")
    public String SalvarFichaAlimentar(@RequestParam(required = false) List<String> produtosSelecionados, @RequestParam int refeicao, Principal principal, HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException {
        return fichaAlimentarFacade.salvar(produtosSelecionados, refeicao, principal, request, response);
    }

    @PostMapping("/removerProdutosFichaAlimentar")
    public String removerProdutoFichaAlimentar(@RequestParam Long id, @RequestParam int refeicao, Principal principal, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        return fichaAlimentarFacade.remover(id, refeicao, principal, request, response);
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
            return "redirect:/home";
        }
        usuarioUseCase.salvarDadosUsuario(dadosUsuarioModel);
        redirectAttributes.addFlashAttribute("fecharModalDados", true);
        return "redirect:/home";
    }

}


