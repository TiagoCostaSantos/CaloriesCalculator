package com.CaloriesCalculator.usecase.impl;

import com.CaloriesCalculator.database.entity.Intensidade;
import com.CaloriesCalculator.database.entity.NivelAtividadeFisica;
import com.CaloriesCalculator.model.DadosNutricionalModal;
import com.CaloriesCalculator.model.MacroNutrientesModel;
import com.CaloriesCalculator.usecase.CalculosUseCase;
import org.springframework.stereotype.Service;

@Service
public class CalculosUseCaseImpl implements CalculosUseCase {

    // GASTO DIARIO PARADO
    @Override
    public Double CalcularTMB(Double peso, Double altura, Integer idade, String sexo) {
        // 1️⃣ Calcular TMB (Mifflin-St Jeor)
       Double tbm = (double) ((10 * peso) + (6.25 * altura) - (5 * idade) + ((sexo.equals("MASC")) ? 5 : -161));
       tbm = Math.round(tbm * 1000.0)/1000.0;
       return tbm;
    }

    // GASTO DIARIO TOTAL (ACRESCENTADO ATIVIDADE FISICA)
    @Override
    public Double CalcularTDEE(NivelAtividadeFisica nivelAtividadeFisica, Double tmb) {

        Double tdee = tmb * nivelAtividadeFisica.getFator();
        tdee = Math.round(tdee * 1000.0) / 1000.0;
        return tdee;
    }

    // O QUANTO É PRECISO COMER, A DEPENDER DO OBJETIVO
    @Override
    public Double CalcularMetaKcalDia(Double Tdee, Intensidade intensidade, boolean deficit){
        Double var;
        if(deficit){
            if(intensidade == Intensidade.PACIFICO){
                var = 0.85; // 0,38KG POR SEMANA
            }else if(intensidade == Intensidade.PADRAO){
                var = 0.80;// 0,50KG POR SEMANA
            }else{
                var = 0.70;// 0,76KG POR SEMANA
            }
        }else{
            if(intensidade == Intensidade.PACIFICO){
                var = 1.15;// 0,38KG POR SEMANA
            }else if(intensidade == Intensidade.PADRAO){
                var = 1.20;// 0,50KG POR SEMANA
            }else{
                var = 1.30;// 0,76KG POR SEMANA
            }
        }
        Double metaKcalDia = Tdee * var;
        metaKcalDia = Math.round(metaKcalDia * 1000.0) / 1000.0;
        return metaKcalDia;
    }

    // OQUE PRECISA COMER, A DEPENDER DO OBJETIVO
    @Override
    public MacroNutrientesModel CalcularMacros(Double metaKcalDia, boolean deficit){
        MacroNutrientesModel macroNutrientesModel = new MacroNutrientesModel();

        if(deficit){
            macroNutrientesModel.setCarboidratos(metaKcalDia * 0.40); // (PORCENTAGEM / 100%)
            macroNutrientesModel.setProteinas(metaKcalDia * 0.33);
            macroNutrientesModel.setGordurasGerais(metaKcalDia * 0.27);

        }else{
            macroNutrientesModel.setCarboidratos(metaKcalDia * 0.54); // (PORCENTAGEM / 100%)
            macroNutrientesModel.setProteinas(metaKcalDia * 0.24);
            macroNutrientesModel.setGordurasGerais(metaKcalDia * 0.22);
        }
        return macroNutrientesModel;
    }

    // EM QUANTO TEMPO, COM ESTE metaKcalDia EU CONSIGO A META PRESCRITA
    @Override
    public Integer QtdDiasMeta(Double metaKcalDia, Double tdee, Double peso ,Double metaPeso, boolean deficit){

        Double KgDia;

        if(deficit){
            KgDia = (tdee - metaKcalDia) / 7700;
            if(KgDia == 0) return 0;
            return (int) Math.ceil((peso - metaPeso) / Math.abs(KgDia));
        } else {
            KgDia = (metaKcalDia - tdee) / 7700;
            if(KgDia == 0) return 0;
            return (int) Math.ceil((metaPeso - peso) / KgDia);
        }

    }

    @Override
    public DadosNutricionalModal CalcularTudo(Double peso, Double altura, Integer idade, String sexo, NivelAtividadeFisica nivelAtividadeFisica, Intensidade intensidade, boolean deficit, Double metaPeso) {

        DadosNutricionalModal dadosNutricionalModal = new DadosNutricionalModal();
        dadosNutricionalModal.setTbm(CalcularTMB(peso, altura, idade, sexo));
        dadosNutricionalModal.setTdee(CalcularTDEE(nivelAtividadeFisica, dadosNutricionalModal.getTbm()));
        dadosNutricionalModal.setMetaKcalDia(CalcularMetaKcalDia(dadosNutricionalModal.getTdee(), intensidade, deficit));
        dadosNutricionalModal.setMacroNutrientesModel(CalcularMacros(dadosNutricionalModal.getMetaKcalDia(), deficit));
        dadosNutricionalModal.setQtdDias(QtdDiasMeta(dadosNutricionalModal.getMetaKcalDia(), dadosNutricionalModal.getTdee(), peso, metaPeso, deficit));

        return dadosNutricionalModal;
    }

}
