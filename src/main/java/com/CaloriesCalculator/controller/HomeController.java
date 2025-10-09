package com.CaloriesCalculator.controller;

import com.CaloriesCalculator.model.ProdutoAlimenticioModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;



@Controller
public class HomeController {

    @GetMapping("/")
    public String mostrarPagIndex(){
        return "index";
    }

    @GetMapping("/home")
    public String mostrarPagHome(){
        return "Home";
    }
}
