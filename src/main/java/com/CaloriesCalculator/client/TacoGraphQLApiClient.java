package com.CaloriesCalculator.client;

import com.CaloriesCalculator.dto.GraphQLResponse;
import com.CaloriesCalculator.dto.ProdutoAlimenticioDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

// Component para o Spring fazer o bean da classe que precisamos para utilizar no construtor, seria um @service, porém ele não é para regra de negocio e sim para consumir a api externa
@Component
public class TacoGraphQLApiClient {
    private final WebClient webClient;
    // TODO FAZER CONFIGURAÇÃO PARA TESTAR CONEXÃO E VERIFICAR, CASO NÃO CONSIGA ACESSAR CONTINUAR COM PRODUTOS CADASTRADOS
    public TacoGraphQLApiClient() {
        this.webClient = WebClient.create("http://localhost:4000/graphql");
    }

    public List<ProdutoAlimenticioDto> buscarProdutoApi(String ProdutoAlimenticio) {
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
        """, ProdutoAlimenticio);
        Map<String, String> body = Map.of("query", query);

        GraphQLResponse response = webClient.post()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(GraphQLResponse.class)
                .block(); // bloqueia até receber a resposta

        return response.getData().getGetFoodByName();
    }

}
