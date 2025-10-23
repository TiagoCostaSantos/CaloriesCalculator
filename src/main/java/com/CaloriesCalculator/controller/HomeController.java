package com.CaloriesCalculator.controller;

import com.CaloriesCalculator.client.TacoGraphQLApiClient;
import com.CaloriesCalculator.database.repository.UsuarioRepository;
import com.CaloriesCalculator.dto.ProdutoAlimenticioDto;
import com.CaloriesCalculator.model.ProdutoAlimenticioModel;
import com.CaloriesCalculator.model.UsuarioModel;
import com.CaloriesCalculator.usecase.UsuarioUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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

    @PostMapping("/autenticar")
    public String autenticarLogin(@ModelAttribute("user") UsuarioModel usuarioModel, Model model){
        boolean validado = usuarioUseCase.autenticarUsuario(usuarioModel.getEmail(), usuarioModel.getPassword());

        if(validado == true){
            return "index";
        }else{
            model.addAttribute("erro", "Email ou senha inválidos");
            return "login";
        }
    }
}
