package com.CaloriesCalculator.controller;

import com.CaloriesCalculator.database.entity.*;
import com.CaloriesCalculator.model.DadosNutricionalModal;
import com.CaloriesCalculator.model.DadosUsuarioModel;
import com.CaloriesCalculator.model.ProdutoAlimenticioModel;
import com.CaloriesCalculator.model.UsuarioModel;
import com.CaloriesCalculator.usecase.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Controller
public class HomeController {
    private final UsuarioUseCase usuarioUseCase;
    private final CookieUseCase cookieUseCase;
    private final CalculosUseCase calculosUseCase;
    private final FichaAlimentarUseCase fichaAlimentarUseCase;
    private final ProdutoAlimenticioUseCase produtoAlimenticioUseCase;

    public HomeController(UsuarioUseCase usuarioUseCase, CookieUseCase cookieUseCase, CalculosUseCase calculosUseCase, FichaAlimentarUseCase fichaAlimentarUseCase, ProdutoAlimenticioUseCase produtoAlimenticioUseCase){
        this.usuarioUseCase = usuarioUseCase;
        this.cookieUseCase = cookieUseCase;
        this.calculosUseCase = calculosUseCase;
        this.fichaAlimentarUseCase = fichaAlimentarUseCase;
        this.produtoAlimenticioUseCase = produtoAlimenticioUseCase;
    }

    @GetMapping({"/", "/login"})
    public String mostrarPagLogin(Model model){
        model.addAttribute("user", new UsuarioModel());
        return "login";
    } // todo verificar se vou usar ainda

    @GetMapping("/home")
    public String mostrarPagIndex(@RequestParam(required = false, defaultValue = "false") boolean abrirModal, DadosUsuarioModel dadosUsuarioModel, Model model, Principal principal, HttpServletRequest request) throws UnsupportedEncodingException {

        List<ProdutoAlimenticioModel> todosProdutosDasRefeicoes = new ArrayList<>();
        Double kcalConsumida = (double) 0;
        Double carboConsumido = (double) 0;
        Double proteinaConsumida = (double) 0;
        Double outroConsumida = (double) 0;
        DadosNutricionalModal dadosNutricionalModal = new DadosNutricionalModal();

        if(principal != null){
            UsuarioEntity user = usuarioUseCase.buscarUsuarioLogado();
            Optional<DadosUsuarioEntity> dadosUsuarioEntity1 = usuarioUseCase.buscarDadosUsuario(user.getId());

            if(dadosUsuarioEntity1.isPresent()){

                DadosUsuarioEntity dadosUsuarioEntity = dadosUsuarioEntity1.get();
                Integer idade = Period.between(user.getDataNascimento(), LocalDate.now()).getYears();
                boolean deficit = dadosUsuarioEntity.getPeso() >= dadosUsuarioEntity.getMetaPeso() ? true : false;
                dadosNutricionalModal = calculosUseCase.CalcularTudo(dadosUsuarioEntity.getPeso(), dadosUsuarioEntity.getAltura(), idade, dadosUsuarioEntity.getSexo(), dadosUsuarioEntity.getNivelAtividadeFisica(), dadosUsuarioEntity.getIntensidade(), deficit, dadosUsuarioEntity.getMetaPeso());
                int qtdDias = dadosNutricionalModal.getQtdDias();
                int qtdGraficos = (int) Math.ceil(qtdDias / 30.0);
                int diaAtualProjeto = (int) ChronoUnit.DAYS.between(dadosUsuarioEntity.getDataCadastroDados(), LocalDate.now());
                model.addAttribute("qtdGraficos", qtdGraficos);
                model.addAttribute("dadosUsuario", dadosNutricionalModal);
                model.addAttribute("diaAtualProjeto", diaAtualProjeto+1);
                model.addAttribute("dadosUsuarioCadastrado", dadosUsuarioEntity);

                // PEGANDO HISTORICO DE MACRO DESDE O COMEÇO DO PROJETO
                List<FichaAlimentarEntity> fichasAlimentares = fichaAlimentarUseCase.buscarFichaAlimentar(user.getId());

                // lista de datas que possuem ficha depois da data de criação da meta
                List<FichaAlimentarEntity> fichasFiltradas = fichasAlimentares.stream()
                        .filter(data -> !data.getData().isBefore(dadosUsuarioEntity.getDataCadastroDados()))
                        .toList();

                // diferença de dias do dia de criação da meta, até hoje.
//                int dias = (int) ChronoUnit.DAYS.between(dadosUsuarioEntity.getDataCadastroDados(), LocalDate.now());

                // dias = dias do dia de cadastro da meta até hoje
                LocalDate dataInicial = dadosUsuarioEntity.getDataCadastroDados();
                List<LocalDate> dias = new ArrayList<>();
                while(!dataInicial.isAfter(LocalDate.now())){
                    dias.add(dataInicial);
                    dataInicial = dataInicial.plusDays(1);
                }

                List<Double> KcalConsumidoTodosOsDia = new ArrayList<>();
                List<Double> CarboConsumidoTodosOsDia = new ArrayList<>();
                List<Double> ProteinaConsumidoTodosOsDia = new ArrayList<>();

                for(LocalDate dia : dias){

                    Double KcalConsumidoNoDia = 0.0;
                    Double CarboConsumidoNoDia = 0.0;
                    Double ProteinaConsumidoNoDia = 0.0;

                    if(fichaAlimentarUseCase.buscarFichaAlimentarDoDia(user.getId(), dia).isPresent()){
                        // se existe ficha daquele dia
                        List<RefeicaoEntity> refeicoesDoDia = fichaAlimentarUseCase.buscarRefeicoesDoDia(user.getId(), dia);
                        for(RefeicaoEntity refeicao : refeicoesDoDia){
                            List<Refeicao_ProdutoAlimenticioEntity> produtosDaRefeicao = fichaAlimentarUseCase.buscarProdutos_Refeicao(refeicao.getId());
                            for(Refeicao_ProdutoAlimenticioEntity produto : produtosDaRefeicao){
                                ProdutoAlimenticioModel produtoAtual = produtoAlimenticioUseCase.buscarProdutoById(produto.getProdutoAlimenticio().getId());
                                KcalConsumidoNoDia += (produtoAtual.getKcal() * produto.getQuantidade());
                                CarboConsumidoNoDia += (produtoAtual.getCarboidratos() * produto.getQuantidade());
                                ProteinaConsumidoNoDia += (produtoAtual.getProteinas() * produto.getQuantidade());
                            }
                        }
                        KcalConsumidoTodosOsDia.add(KcalConsumidoNoDia);
                        CarboConsumidoTodosOsDia.add(CarboConsumidoNoDia);
                        ProteinaConsumidoTodosOsDia.add(ProteinaConsumidoNoDia);
                    }else{
                        // se não existe ficha do dia, ai pega do dia anterior mais proximo
                        Optional<FichaAlimentarEntity> dataMaisProximaAnterior = fichasFiltradas.stream()
                                .filter(f -> f.getData().isBefore(dia))// só datas anteriores
                                .max(Comparator.comparing(FichaAlimentarEntity::getData)); // pega a maior data

                        if(dataMaisProximaAnterior.isPresent()){
                            // se existir uma data anterior
                            List<RefeicaoEntity> refeicoesDoDia = fichaAlimentarUseCase.buscarRefeicoesDoDia(user.getId(), dataMaisProximaAnterior.get().getData());
                            for(RefeicaoEntity refeicao : refeicoesDoDia){
                                List<Refeicao_ProdutoAlimenticioEntity> produtosDaRefeicao = fichaAlimentarUseCase.buscarProdutos_Refeicao(refeicao.getId());
                                for(Refeicao_ProdutoAlimenticioEntity produto : produtosDaRefeicao){
                                    ProdutoAlimenticioModel produtoAtual = produtoAlimenticioUseCase.buscarProdutoById(produto.getProdutoAlimenticio().getId());
                                    KcalConsumidoNoDia += (produtoAtual.getKcal() * produto.getQuantidade());
                                    CarboConsumidoNoDia += (produtoAtual.getCarboidratos() * produto.getQuantidade());
                                    ProteinaConsumidoNoDia += (produtoAtual.getProteinas() * produto.getQuantidade());
                                }
                            }

                            KcalConsumidoTodosOsDia.add(KcalConsumidoNoDia);
                            CarboConsumidoTodosOsDia.add(CarboConsumidoNoDia);
                            ProteinaConsumidoTodosOsDia.add(ProteinaConsumidoNoDia);

                        }else{
                            // se não existir
                            KcalConsumidoTodosOsDia.add(0.0);
                            CarboConsumidoTodosOsDia.add(0.0);
                            ProteinaConsumidoTodosOsDia.add(0.0);
                        }
                    }
                }

                model.addAttribute("KcalConsumidoTodosOsDia", KcalConsumidoTodosOsDia);
                model.addAttribute("CarboConsumidoTodosOsDia", CarboConsumidoTodosOsDia);
                model.addAttribute("ProteinaConsumidoTodosOsDia", ProteinaConsumidoTodosOsDia);

            }else{
                model.addAttribute("AcionarModalDados", abrirModal);
            }

            List<ProdutoAlimenticioModel> cafe = new ArrayList<>();
            List<ProdutoAlimenticioModel> almoco = new ArrayList<>();
            List<ProdutoAlimenticioModel> lanche = new ArrayList<>();
            List<ProdutoAlimenticioModel> janta = new ArrayList<>();
            List<ProdutoAlimenticioModel> suplementacao = new ArrayList<>();
            List<ProdutoAlimenticioModel> outro = new ArrayList<>();

            FichaAlimentarEntity fichaMaisRecente = fichaAlimentarUseCase.buscarFichaAlimentarMaisRecente(user.getId());
            boolean adicionarASession = false;
            if (fichaMaisRecente != null && !Objects.equals(fichaMaisRecente.getData(), LocalDate.now())) {
                adicionarASession = true;
            }

            List<RefeicaoEntity> refeicoesDoDia = fichaAlimentarUseCase.buscarRefeicoesDoDia(user.getId(), (fichaMaisRecente != null && !Objects.equals(fichaMaisRecente.getData(), LocalDate.now()) ? fichaMaisRecente.getData() : LocalDate.now()));
            List<Refeicao_ProdutoAlimenticioEntity> listaProdutosDaRefeicao = new ArrayList<>();

            for(RefeicaoEntity refeicao : refeicoesDoDia){

                // lista de produtos de tal refeicao mas da tabela refeicao_produtoAlimenticio, ai precisamos buscar as inf
                listaProdutosDaRefeicao = fichaAlimentarUseCase.buscarProdutos_Refeicao(refeicao.getId());

                switch (refeicao.getTipo()){
                    case CAFE ->{
                        for(Refeicao_ProdutoAlimenticioEntity produto : listaProdutosDaRefeicao){
                            var produtoBuscado = produtoAlimenticioUseCase.buscarProdutoById(produto.getProdutoAlimenticio().getId());
                            for(int i = 0; i < produto.getQuantidade(); i++){
                                cafe.add(produtoBuscado);
                            }
                            if (adicionarASession) {
                                fichaAlimentarUseCase.adicionarRefeicaoProdutoSession(1, produto.getProdutoAlimenticio().getId());
                            }
                        }
                    }
                    case ALMOCO -> {
                        for(Refeicao_ProdutoAlimenticioEntity produto : listaProdutosDaRefeicao){
                            var produtoBuscado = produtoAlimenticioUseCase.buscarProdutoById(produto.getProdutoAlimenticio().getId());
                            for(int i = 0; i < produto.getQuantidade(); i++){
                                almoco.add(produtoBuscado);
                            }
                            if (adicionarASession) {
                                fichaAlimentarUseCase.adicionarRefeicaoProdutoSession(2, produto.getProdutoAlimenticio().getId());
                            }
                        }
                    }
                    case LANCHE -> {
                        for(Refeicao_ProdutoAlimenticioEntity produto : listaProdutosDaRefeicao){
                            var produtoBuscado = produtoAlimenticioUseCase.buscarProdutoById(produto.getProdutoAlimenticio().getId());
                            for(int i = 0; i < produto.getQuantidade(); i++){
                                lanche.add(produtoBuscado);
                            }
                            if (adicionarASession) {
                                fichaAlimentarUseCase.adicionarRefeicaoProdutoSession(3, produto.getProdutoAlimenticio().getId());
                            }
                        }
                    }
                    case JANTA -> {
                        for(Refeicao_ProdutoAlimenticioEntity produto : listaProdutosDaRefeicao){
                            var produtoBuscado = produtoAlimenticioUseCase.buscarProdutoById(produto.getProdutoAlimenticio().getId());
                            for(int i = 0; i < produto.getQuantidade(); i++){
                                janta.add(produtoBuscado);
                            }
                            if (adicionarASession) {
                                fichaAlimentarUseCase.adicionarRefeicaoProdutoSession(4, produto.getProdutoAlimenticio().getId());
                            }
                        }
                    }
                    case SUPLEMENTO -> {
                        for(Refeicao_ProdutoAlimenticioEntity produto : listaProdutosDaRefeicao){
                            var produtoBuscado = produtoAlimenticioUseCase.buscarProdutoById(produto.getProdutoAlimenticio().getId());
                            for(int i = 0; i < produto.getQuantidade(); i++){
                                suplementacao.add(produtoBuscado);
                            }
                            if (adicionarASession) {
                                fichaAlimentarUseCase.adicionarRefeicaoProdutoSession(5, produto.getProdutoAlimenticio().getId());
                            }
                        }
                    }
                    case OUTRO -> {
                        for(Refeicao_ProdutoAlimenticioEntity produto : listaProdutosDaRefeicao){
                            var produtoBuscado = produtoAlimenticioUseCase.buscarProdutoById(produto.getProdutoAlimenticio().getId());
                            for(int i = 0; i < produto.getQuantidade(); i++){
                                outro.add(produtoBuscado);
                            }
                            if (adicionarASession) {
                                fichaAlimentarUseCase.adicionarRefeicaoProdutoSession(6, produto.getProdutoAlimenticio().getId());
                            }
                        }
                    }
                }
            }

            List<ProdutoAlimenticioModel> todos = new ArrayList<>();
            todos.addAll(cafe);
            todos.addAll(almoco);
            todos.addAll(lanche);
            todos.addAll(janta);
            todos.addAll(suplementacao);
            todos.addAll(outro);

            for(ProdutoAlimenticioModel produto : todos){
                kcalConsumida += produto.getKcal();
                carboConsumido += produto.getCarboidratos();
                proteinaConsumida += produto.getProteinas();
                outroConsumida += produto.getGorduraGerais();
            }

            if(!dadosNutricionalModal.isEmpty()){
                model.addAttribute("kcalFaltante", Math.max(dadosNutricionalModal.getMetaKcalDia() - kcalConsumida, 0));
                model.addAttribute("carboFaltante", Math.max(dadosNutricionalModal.getMacroNutrientesModel().getCarboidratos() - carboConsumido, 0));
                model.addAttribute("proteinaFaltante", Math.max(dadosNutricionalModal.getMacroNutrientesModel().getProteinas() - proteinaConsumida, 0));
                model.addAttribute("outroFaltante", Math.max(dadosNutricionalModal.getMacroNutrientesModel().getGordurasGerais() - outroConsumida, 0));
            }

            model.addAttribute("cafe", cafe);
            model.addAttribute("almoco", almoco);
            model.addAttribute("lanche", lanche);
            model.addAttribute("janta", janta);
            model.addAttribute("suplementacao", suplementacao);
            model.addAttribute("outro", outro);

        }else{

            for(int i = 0; i < 6; i++){

                List<Long> produtosPorRefeicao = cookieUseCase.lerProdutosRefeicaoCookie(i, "FichaAlimentar", request);
                List<ProdutoAlimenticioModel> produtosModel = new ArrayList<>();

                for(Long produto : produtosPorRefeicao){
                    ProdutoAlimenticioModel produto2 = produtoAlimenticioUseCase.buscarProdutoById(produto);

                    if(produto2 != null){
                        produtosModel.add(produto2);
                        todosProdutosDasRefeicoes.add(produto2);
                    }
                }

                switch (i){
                    case 0: model.addAttribute("cafe", produtosModel);
                        break;
                    case 1: model.addAttribute("almoco", produtosModel);
                        break;
                    case 2: model.addAttribute("lanche", produtosModel);
                        break;
                    case 3: model.addAttribute("janta", produtosModel);
                        break;
                    case 4: model.addAttribute("suplementacao", produtosModel);
                        break;
                    case 5: model.addAttribute("outro", produtosModel);
                        break;
                }
            }

            for(ProdutoAlimenticioModel produto : todosProdutosDasRefeicoes){
                kcalConsumida += produto.getKcal();
                carboConsumido += produto.getCarboidratos();
                proteinaConsumida += produto.getProteinas();
                outroConsumida += produto.getGorduraGerais();
            }

        }
        // todo tirar funções do controller colocar em services
        model.addAttribute("kcalConsumida", kcalConsumida);
        model.addAttribute("carboConsumido", carboConsumido);
        model.addAttribute("proteinaConsumida", proteinaConsumida);
        model.addAttribute("outroConsumida", outroConsumida);

        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", new UsuarioModel());
        }
        return "index";
    }

    @GetMapping("/meuPerfil")
    public String mostrarPagMeuPerfil(Principal principal, Model model){
        Optional<UsuarioEntity> user = usuarioUseCase.buscarPorEmail(principal.getName());
        UsuarioEntity usuario = user.get();
        model.addAttribute("usuario", usuario);
        return "meuPerfil";
    }

    @GetMapping("/sobre")
    public String mostrarPagSobre(){
        return "sobre";
    }
}
