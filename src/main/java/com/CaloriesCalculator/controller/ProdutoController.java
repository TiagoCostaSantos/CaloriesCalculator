package com.CaloriesCalculator.controller;
import com.CaloriesCalculator.model.ProdutoAlimenticioModel;
import com.CaloriesCalculator.usecase.ProdutoAlimenticioUseCase;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping("/produto-alimenticio")
public class ProdutoController {

    private final ProdutoAlimenticioUseCase produtoAlimenticioUseCase;

    public ProdutoController(ProdutoAlimenticioUseCase produtoAlimenticioUseCase){
        this.produtoAlimenticioUseCase = produtoAlimenticioUseCase;
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
    public String BuscarProdutos(@RequestParam(value = "produtoAlimenticio", required = false) String produtoAlimenticio,@RequestParam(value = "refeicao", required = false) String refeicao, Model model){

        List<ProdutoAlimenticioModel> ProdutosListaBd;

        if(produtoAlimenticio == null || produtoAlimenticio.isEmpty()){
            ProdutosListaBd = produtoAlimenticioUseCase.todosProdutos();
        }else{
            ProdutosListaBd = produtoAlimenticioUseCase.buscarProduto(produtoAlimenticio);
        }

        model.addAttribute("ListaGeral", ProdutosListaBd);
        model.addAttribute("refeicao", refeicao);
        return "fragments/retornoProdutos :: conteudo";
    }

}
