package com.CaloriesCalculator.model;

public class DadosNutricionalModal {

    private Double tbm;
    private Double tdee;
    private MacroNutrientesModel macroNutrientesModel;
    private Double metaKcalDia;
    private Integer qtdDias;

    public boolean isEmpty() {
        return metaKcalDia == null &&
                qtdDias == null &&
                macroNutrientesModel == null &&
                tdee == null &&
                tbm == null;
    }

    public Double getTbm() {
        return tbm;
    }

    public void setTbm(Double tbm) {
        this.tbm = tbm;
    }

    public Double getTdee() {
        return tdee;
    }

    public void setTdee(Double tdee) {
        this.tdee = tdee;
    }

    public MacroNutrientesModel getMacroNutrientesModel() {
        return macroNutrientesModel;
    }

    public void setMacroNutrientesModel(MacroNutrientesModel macroNutrientesModel) {
        this.macroNutrientesModel = macroNutrientesModel;
    }

    public Double getMetaKcalDia() {
        return metaKcalDia;
    }

    public void setMetaKcalDia(Double metaKcalDia) {
        this.metaKcalDia = metaKcalDia;
    }

    public Integer getQtdDias() {
        return qtdDias;
    }

    public void setQtdDias(Integer qtdDias) {
        this.qtdDias = qtdDias;
    }
}
