package com.CaloriesCalculator.controller;

import com.CaloriesCalculator.client.TacoGraphQLApiClient;
import com.CaloriesCalculator.model.UsuarioModel;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;

@Controller
public class HomeController {

    private final TacoGraphQLApiClient tacoGraphQLApiClient;
    private final UsuarioUseCase usuarioUseCase;

    public HomeController(TacoGraphQLApiClient tacoGraphQLApiClient, UsuarioUseCase usuarioUseCase){
        this.tacoGraphQLApiClient = tacoGraphQLApiClient;
        this.usuarioUseCase = usuarioUseCase;
    }

    @GetMapping({"/", "/login"})
    public String mostrarPagLogin(Model model){
        model.addAttribute("user", new UsuarioModel());
        return "login";
    }

    @GetMapping("/home")
    public String mostrarPagIndex(Principal principal, Model model){
        model.addAttribute("usuarioLogado", principal.getName());
        return "index";
    }
}
