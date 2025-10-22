package com.CaloriesCalculator.controller;

import com.CaloriesCalculator.client.TacoGraphQLApiClient;
import com.CaloriesCalculator.dto.ProdutoAlimenticioDto;
import com.CaloriesCalculator.model.ProdutoAlimenticioModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class HomeController {

    private final TacoGraphQLApiClient tacoGraphQLApiClient;

    public HomeController(TacoGraphQLApiClient tacoGraphQLApiClient){
        this.tacoGraphQLApiClient = tacoGraphQLApiClient;
    }

    @GetMapping("/")
    public String mostrarPagIndex(){
        return "index";
    }

}
