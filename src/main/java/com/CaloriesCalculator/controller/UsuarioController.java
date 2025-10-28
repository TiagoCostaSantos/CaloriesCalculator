package com.CaloriesCalculator.controller;

import com.CaloriesCalculator.database.entity.UsuarioEntity;
import com.CaloriesCalculator.model.UsuarioModel;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

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
    public String SalvarCadastroUsuario(@ModelAttribute("usuario") UsuarioModel usuarioModel, Principal principal){
        if(principal == null){
            // não logado
            if(usuarioUseCase.cadastrarUsuario(usuarioModel)){
                return "redirect:/login?cadastrado";
            }
            return "redirect:/usuario/cadastrar?emailExists";
        }else{
            usuarioUseCase.atualizarUsuario(usuarioModel, principal.getName());
            return "redirect:/meuPerfil?dadosAtualizados";
        }
    }
}

// TODO FAZER VALIDAÇÃO DOS VALORES RECEBIDOS,

// as validações @Pattern, @size, deveram ficar no model, para que seja feita a validação pelo meotodo @valid, e a entity apenas os (@Column, nullable etc.) pois seriam regras gerais de quando for gerar o bd, ai eles ja irão formatados para a entity

//@PostMapping("/usuario/salvar")
//public String salvar(@Valid @ModelAttribute("usuario") UsuarioModel usuario, BindingResult result) {
//    if (result.hasErrors()) {
//        return "usuario-form";
//    }
