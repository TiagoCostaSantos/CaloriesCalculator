package com.CaloriesCalculator.controller;

import com.CaloriesCalculator.model.ProdutoAlimenticioModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/produto-alimenticio")
public class ProdutoController {

    // CRUD Produtos alimenticios

    //Chamando o formulario, e instanciando o model
    @GetMapping("/Cadastrar")
    public String formCadastro (Model model){
        // aqui colocamos o nome do atributo produto
        model.addAttribute("produtoAlimenticio",new ProdutoAlimenticioModel());
        return "cadastroProdutoAlimenticio";
    }

    @PostMapping("/salvar")
    public String SalvarCadastroProdutoAlimenticio(@ModelAttribute("produtoAlimenticio") ProdutoAlimenticioModel produtoAlimenticio){
        System.out.println("id" + produtoAlimenticio.getId());
        System.out.println("titulo" + produtoAlimenticio.getTitulo());
        System.out.println("tipo" + produtoAlimenticio.getTipo());
        System.out.println("kcal" + produtoAlimenticio.getKcal());
        System.out.println("carboidratos" + produtoAlimenticio.getCarboidratos());
        System.out.println("proteinas" + produtoAlimenticio.getProteinas());
        System.out.println("gordurasGerais" + produtoAlimenticio.getGorduraGerais());
        System.out.println("peso" + produtoAlimenticio.getPeso());
        return "teste";
    }

}
