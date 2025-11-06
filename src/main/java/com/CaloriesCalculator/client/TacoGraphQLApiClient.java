package com.CaloriesCalculator.client;

import com.CaloriesCalculator.dto.*;
import com.CaloriesCalculator.model.ProdutoAlimenticioModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import java.time.Duration;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClientRequestException;

// Component para o Spring fazer o bean da classe que precisamos para utilizar no construtor, seria um @service, porém ele não é para regra de negocio e sim para consumir a api externa
@Component
public class TacoGraphQLApiClient {

    private final WebClient webClient;

    public TacoGraphQLApiClient() {
        this.webClient = WebClient.create("http://localhost:4000/graphql");
    }

    public boolean testarConexao() {
        String queryTeste = "{ __typename }";
        Map<String, String> body = Map.of("query", queryTeste);

        try {
            webClient.post()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(3))
                    .block(); // ⚠ aqui bloqueia e captura a exceção
            return true;
        } catch (WebClientRequestException e) {
            System.err.println("❌ API offline: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("❌ Outro erro: " + e.getMessage());
        }
        return false;
    }

    public ProdutoAlimenticioModel buscarProdutoApiId(Long id){
        if(!testarConexao()){
            System.out.println("⚠ API offline — usando produtos do banco de dados.");
            return null; // fallback
        }

        String query = String.format("""
        {
          getFoodById(id: %d) {
            name
            category { name }
            nutrients {
              kcal
              carbohydrates
              protein
            }
          }
        }
        """, id);

        Map<String, String> body = Map.of("query", query);

        GraphQLResponse<GetFoodByIdWrapper> response = webClient.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<GraphQLResponse<GetFoodByIdWrapper>>() {})
                .block();

        return ConvertDTOtoModel(response.getData().getGetFoodById());
    }

    public List<ProdutoAlimenticioModel> buscarProdutoApi(String ProdutoAlimenticio) {
        if(!testarConexao()){
            System.out.println("⚠ API offline — usando produtos do banco de dados.");
            return List.of(); // fallback
        }
            String ProdutoAlimenticioFormatado = ProdutoAlimenticio.replace(" ", ", ");
            String query = String.format("""
            {
              getFoodByName(name: "%s") {
                id
                name
                category { name }
                nutrients {
                  kcal
                  carbohydrates
                  protein
                }
              }
            }
            """, ProdutoAlimenticioFormatado);

            Map<String, String> body = Map.of("query", query);

            GraphQLResponse<GetFoodByNameWrapper> response = webClient.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<GraphQLResponse<GetFoodByNameWrapper>>() {})
                .block();

            return ConvertDTOtoModelList(response.getData().getGetFoodByName());
    }

    public List<ProdutoAlimenticioModel> buscarTodosProdutosApi(){
        if(!testarConexao()){
            System.out.println("⚠ API offline — usando produtos do banco de dados.");
            return List.of(); // fallback
        }
        String query;
        query = """
            {
              getAllFood {
                id
                name
                category { name }
                nutrients { kcal carbohydrates protein }
              }
            }
            """;

        Map<String, String> body = Map.of("query", query);

        GraphQLResponse<GetAllFoodWrapper> response = webClient.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<GraphQLResponse<GetAllFoodWrapper>>() {})
                .block();
        List<ProdutoAlimenticioModel> ListaDeProdutos = ConvertDTOtoModelList(response.getData().getGetAllFood());
        return ListaDeProdutos;
    }

    public ProdutoAlimenticioModel ConvertDTOtoModel(ProdutoAlimenticioDto produtoAlimenticioDto){

            ProdutoAlimenticioModel produtoAlimenticioModel = new ProdutoAlimenticioModel();
            produtoAlimenticioModel.setId(produtoAlimenticioDto.getId());
            produtoAlimenticioModel.setTipoApi(true);
            String tituloFormatado = produtoAlimenticioDto.getName().replace(",", "").trim();
            produtoAlimenticioModel.setTitulo(tituloFormatado);
            produtoAlimenticioModel.setTipo(produtoAlimenticioDto.getCategory().getName());
            // Tratando NullException com if ternario
            produtoAlimenticioModel.setKcal(produtoAlimenticioDto.getNutrients().getKcal() == null ? 0.0 : produtoAlimenticioDto.getNutrients().getKcal());
            produtoAlimenticioModel.setCarboidratos(produtoAlimenticioDto.getNutrients().getCarbohydrates() == null ? 0.0 : produtoAlimenticioDto.getNutrients().getCarbohydrates());
            produtoAlimenticioModel.setProteinas(produtoAlimenticioDto.getNutrients().getProtein() == null ? 0.0 : produtoAlimenticioDto.getNutrients().getProtein());

            return produtoAlimenticioModel;
    }

    public List<ProdutoAlimenticioModel> ConvertDTOtoModelList(List<ProdutoAlimenticioDto> produtoAlimenticioDto){
        return produtoAlimenticioDto.stream().map(this::ConvertDTOtoModel).toList();
    }
}
