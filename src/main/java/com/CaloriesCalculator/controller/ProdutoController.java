package com.CaloriesCalculator.controller;
import com.CaloriesCalculator.client.TacoGraphQLApiClient;
import com.CaloriesCalculator.dto.ProdutoAlimenticioDto;
import com.CaloriesCalculator.model.ProdutoAlimenticioModel;
import com.CaloriesCalculator.usecase.ProdutoAlimenticioUseCase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/produto-alimenticio")
public class ProdutoController {

    private final ProdutoAlimenticioUseCase produtoAlimenticioUseCase;

    private final TacoGraphQLApiClient tacoGraphQLApiClient;

    public ProdutoController(ProdutoAlimenticioUseCase produtoAlimenticioUseCase, TacoGraphQLApiClient tacoGraphQLApiClient){
        this.produtoAlimenticioUseCase = produtoAlimenticioUseCase;
        this.tacoGraphQLApiClient = tacoGraphQLApiClient;
    }
    // CRUD Produtos alimenticios

    //Chamando o formulario, e instanciando o model
    @GetMapping("/cadastrar")
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
    // List<ProdutoAlimenticioDto>
    @GetMapping("/BuscarProdutoApi")
    public String BuscarProdutoApi(@RequestParam("produtoAlimenticio") String produtoAlimenticio, Model model){
        List<ProdutoAlimenticioDto> ProdutosLista = tacoGraphQLApiClient.buscarProdutoApi(produtoAlimenticio);
        model.addAttribute("ProdutosLista", ProdutosLista);
        return "home";
    }
}
