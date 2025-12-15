package com.CaloriesCalculator.facade;

import com.CaloriesCalculator.model.UsuarioModel;
import com.CaloriesCalculator.service.CaptchaService;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Service
public class UsuarioFacade {

    @Autowired
    private UsuarioUseCase usuarioUseCase;
    @Autowired private CaptchaService captchaService;

    public String salvar(UsuarioModel usuarioModel, BindingResult result, String captchaResponse, String origem, Principal principal, RedirectAttributes redirect, Model model) {

        // 1) Erros de validação
        if (result.hasErrors()) {
            if ("cadastro".equals(origem)) {
                redirect.addFlashAttribute("abrirModalCadastro", true);
                redirect.addFlashAttribute("usuario", usuarioModel); // ← IMPORTANTE
                redirect.addFlashAttribute("erros", result.getAllErrors());
                return "redirect:/home";
            }
            model.addAttribute("erros", result.getAllErrors());
            return "meuPerfil";
        }

        // 2) Usuário não logado → Cadastro
        if (principal == null) {
            return processarCadastro(usuarioModel, captchaResponse, redirect);
        }

        // 3) Usuário logado → Atualização
        return processarAtualizacao(usuarioModel, principal);
    }

    private String processarCadastro(
            UsuarioModel usuarioModel,
            String captchaResponse,
            RedirectAttributes redirect) {

        if (!captchaService.isCaptchaValid(captchaResponse)) {
            redirect.addFlashAttribute("abrirModalCadastro", true);
            return "redirect:/home?errorCaptcha";
        }

        if (usuarioUseCase.cadastrarUsuario(usuarioModel)) {
            return "redirect:/home?modalLogin=true&cadastrado";
        }

        redirect.addFlashAttribute("abrirModalCadastro", true);
        redirect.addFlashAttribute("emailExists", true);
        return "redirect:/home";
    }

    private String processarAtualizacao(UsuarioModel usuarioModel, Principal principal) {
        usuarioUseCase.atualizarUsuario(usuarioModel, principal.getName());
        return "redirect:/meuPerfil?dadosAtualizados";
    }

}
