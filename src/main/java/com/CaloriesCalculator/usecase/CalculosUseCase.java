package com.CaloriesCalculator.usecase;

import com.CaloriesCalculator.database.entity.Intensidade;
import com.CaloriesCalculator.database.entity.NivelAtividadeFisica;
import com.CaloriesCalculator.model.DadosNutricionalModal;
import com.CaloriesCalculator.model.MacroNutrientesModel;

public interface CalculosUseCase {

    Double CalcularTMB(Double peso, Double altura, Integer idade, String sexo);

    Double CalcularTDEE(NivelAtividadeFisica nivelAtividadeFisica,Double tmb);

    Double CalcularMetaKcalDia(Double Tdee, Intensidade intensidade, boolean deficit);

    MacroNutrientesModel CalcularMacros(Double superavit, boolean deficit);

    Integer QtdDiasMeta(Double superavit, Double tdee, Double peso ,Double metaPeso, boolean deficit);

    DadosNutricionalModal CalcularTudo(Double peso, Double altura, Integer idade, String sexo, NivelAtividadeFisica nivelAtividadeFisica, Intensidade intensidade, boolean deficit, Double metaPeso);
}
