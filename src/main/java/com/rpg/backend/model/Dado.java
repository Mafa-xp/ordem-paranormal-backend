package com.rpg.backend.model;

import java.util.Random;

public class Dado {
    private Integer lados;
    private Integer resultado;

    public Dado(Integer lados) {
        this.lados = lados;
    }

    public void rolar() {
        Random aleatorio = new Random();
        resultado = aleatorio.nextInt(lados) + 1;
    }

    public Integer getResultado() {
        return resultado;
    }

    public void setResultados(Integer resultado) { this.resultado = resultado; }
}