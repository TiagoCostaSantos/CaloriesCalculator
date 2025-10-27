package com.CaloriesCalculator.controller;

import com.CaloriesCalculator.model.UsuarioModel;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioUseCase usuarioUseCase;

    public UsuarioController(UsuarioUseCase usuarioUseCase){
        this.usuarioUseCase = usuarioUseCase;
    }

    @GetMapping("/cadastrar")
    public String FormCadastro(Model model){
        model.addAttribute("usuario", new UsuarioModel());
        return "cadastroUsuario";
    }

    @PostMapping("/salvar")
    public String SalvarCadastroUsuario(@ModelAttribute("usuario") UsuarioModel usuarioModel){
        usuarioUseCase.cadastrarUsuario(usuarioModel);
        return "redirect:/login";
    }
}
