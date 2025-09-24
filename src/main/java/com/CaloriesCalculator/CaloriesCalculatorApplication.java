package com.CaloriesCalculator;

import com.CaloriesCalculator.client.TacoGraphQLApiClient;
import com.CaloriesCalculator.dto.ProdutoAlimenticioDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class CaloriesCalculatorApplication {

	public static void main(String[] args) {
		SpringApplication.run(CaloriesCalculatorApplication.class, args);

		/* para testar a API (deve estar rodando na porta 4000
		TacoGraphQLApiClient client = new TacoGraphQLApiClient();
		List<ProdutoAlimenticioDto> alimentos = client.buscarProdutoApi();

		ProdutoAlimenticioDto banana = alimentos.get(0); // primeiro item da lista
		System.out.println("Nome: " + banana.getName());
		System.out.println("ID: " + banana.getId());
		System.out.println("Categoria: " + banana.getCategory().getName());
		System.out.println("carboidratos: " + banana.getNutrients().getCarbohydrates());
		System.out.println("Kcal: " + banana.getNutrients().getKcal());
		System.out.println("protein: " + banana.getNutrients().getProtein());
		System.out.println("Class?: " + banana.getClass());

		 */
	}


}
