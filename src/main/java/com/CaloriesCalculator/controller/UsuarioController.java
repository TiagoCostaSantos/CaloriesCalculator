package com.CaloriesCalculator.controller;

import com.CaloriesCalculator.database.entity.UsuarioEntity;
import com.CaloriesCalculator.model.UsuarioModel;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import java.security.Principal;
import java.time.LocalDate;
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
            }else{
                usuarioUseCase.atualizarUsuario(usuarioModel, principal.getName());
                return "redirect:/meuPerfil?dadosAtualizados";
            }

    }
}
