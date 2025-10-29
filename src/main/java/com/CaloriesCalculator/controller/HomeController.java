package com.CaloriesCalculator.controller;

import com.CaloriesCalculator.client.TacoGraphQLApiClient;
import com.CaloriesCalculator.database.entity.UsuarioEntity;
import com.CaloriesCalculator.model.UsuarioModel;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;
import java.util.Optional;

@Controller
public class HomeController {
    private final UsuarioUseCase usuarioUseCase;
    public HomeController(UsuarioUseCase usuarioUseCase){
        this.usuarioUseCase = usuarioUseCase;
    }

    @GetMapping({"/", "/login"})
    public String mostrarPagLogin(Model model){
        model.addAttribute("user", new UsuarioModel());
        return "login";
    }

    @GetMapping("/home")
    public String mostrarPagIndex(Model model){
        return "index";
    }

    @GetMapping("/meuPerfil")
    public String mostrarPagMeuPerfil(Principal principal, Model model){
        Optional<UsuarioEntity> user = usuarioUseCase.buscarPorEmail(principal.getName());
        UsuarioEntity usuario = user.get();
        model.addAttribute("usuario", usuario);
        return "meuPerfil";
    }
}
