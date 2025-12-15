package com.CaloriesCalculator.facade.viewmodel;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class HomeViewModel {

    private boolean abrirModalDados;
    private boolean abrirModalCadastroUsuario;

    private Map<String, Object> atributos = new HashMap<>();

    public void add(String key, Object value) {
        atributos.put(key, value);
    }

    public Map<String, Object> toMap() {
        return atributos;
    }
}
