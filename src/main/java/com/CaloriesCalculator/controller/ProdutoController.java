package com.CaloriesCalculator.controller;
import com.CaloriesCalculator.client.TacoGraphQLApiClient;
import com.CaloriesCalculator.dto.ProdutoAlimenticioDto;
import com.CaloriesCalculator.model.ProdutoAlimenticioModel;
import com.CaloriesCalculator.usecase.ProdutoAlimenticioUseCase;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Stream;

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
    public String SalvarCadastroProdutoAlimenticio(@Valid @ModelAttribute("produtoAlimenticio") ProdutoAlimenticioModel produtoAlimenticio, BindingResult result, Model model){
        if(result.hasErrors()){
            model.addAttribute("erros", result.getAllErrors());
            return "cadastroProdutoAlimenticio";
        }
        produtoAlimenticioUseCase.cadastrarProdutoAlimenticio(produtoAlimenticio);
        return "redirect:../home?cadastro=sucesso";
    }

    // Busca na API e no Banco de Dados H2
    @GetMapping("/buscar-produto")
    public String BuscarProdutos(@RequestParam(value = "produtoAlimenticio", required = false) String produtoAlimenticio, Model model){

        List<ProdutoAlimenticioModel> ProdutosListaApi;
        List<ProdutoAlimenticioModel> ProdutosListaBd;

        if(produtoAlimenticio == null || produtoAlimenticio.isEmpty()){
            ProdutosListaBd = produtoAlimenticioUseCase.todosProdutos();
            ProdutosListaApi = tacoGraphQLApiClient.buscarTodosProdutosApi();
        }else{
            ProdutosListaBd = produtoAlimenticioUseCase.buscarProduto(produtoAlimenticio);
            ProdutosListaApi = tacoGraphQLApiClient.buscarProdutoApi(produtoAlimenticio);
        }

        //Concatenando as listas
        List<ProdutoAlimenticioModel> ListaGeral = Stream.concat(ProdutosListaBd.stream(), ProdutosListaApi.stream()).toList();
        model.addAttribute("ProdutosLista", ListaGeral);

        return "buscarProduto";
    }
}
