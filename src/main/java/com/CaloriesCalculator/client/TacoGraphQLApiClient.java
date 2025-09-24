package com.CaloriesCalculator.client;

import com.CaloriesCalculator.dto.GraphQLResponse;
import com.CaloriesCalculator.dto.ProdutoAlimenticioDto;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;

public class TacoGraphQLApiClient {
    private final WebClient webClient;

    public TacoGraphQLApiClient() {
        this.webClient = WebClient.create("http://localhost:4000/graphql");
    }

    public List<ProdutoAlimenticioDto> buscarProdutoApi() {
        String query = "{ getFoodByName(name : \"Banana, da terra, crua\"){\n" +
                "    id\n" +
                "    name\n" +
                "    category{name}\n" +
                "    nutrients{\n" +
                "      kcal\n" +
                "      carbohydrates\n" +
                "      protein\n" +
                "    }\n" +
                "  }}";
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
