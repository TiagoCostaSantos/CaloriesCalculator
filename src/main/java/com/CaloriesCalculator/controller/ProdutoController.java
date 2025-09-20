package com.CaloriesCalculator.controller;
import com.CaloriesCalculator.model.ProdutoAlimenticioModel;
import com.CaloriesCalculator.usecase.ProdutoAlimenticioUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/produto-alimenticio")
public class ProdutoController {

    private final ProdutoAlimenticioUseCase produtoAlimenticioUseCase;

    public ProdutoController(ProdutoAlimenticioUseCase produtoAlimenticioUseCase){
        this.produtoAlimenticioUseCase = produtoAlimenticioUseCase;
    }
    // CRUD Produtos alimenticios

    //Chamando o formulario, e instanciando o model
    @GetMapping("/Cadastrar")
    public String formCadastro (Model model){
        // aqui colocamos o nome do atributo produto para o thymeleaf
        model.addAttribute("produtoAlimenticio",new ProdutoAlimenticioModel());
        return "cadastroProdutoAlimenticio";
    }

    @PostMapping("/salvar")
    public String SalvarCadastroProdutoAlimenticio(@ModelAttribute("produtoAlimenticio") ProdutoAlimenticioModel produtoAlimenticio){
        produtoAlimenticioUseCase.cadastrarProdutoAlimenticio(produtoAlimenticio);
        return "redirect:../?cadastro=sucesso";
    }
}
