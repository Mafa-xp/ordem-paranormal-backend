package com.rpg.backend.model;

import com.rpg.backend.enums.NivelInimigo;
import java.util.Objects;

public class Inimigo implements Acao {
    private Integer vidaInimigo;
    private Integer vidaMaxInimigo;
    private Integer forca;
    private NivelInimigo nivel;

    private boolean sucessoNoDado = false;
    private Integer resultadoAtaque = 0;
    private Integer resultadoDefesa = 0;

    Dado dado20 = new Dado(20);
    Dado dado6 = new Dado(6);

    public Inimigo(NivelInimigo nivel) {
        this.nivel = nivel;
        definirStatus();
    }

    void definirStatus() {
        switch (nivel) {
            case FACIL:
                vidaInimigo = 20;
                vidaMaxInimigo = 20;
                forca = 2;
                break;
            case MEDIO:
                vidaInimigo = 25;
                vidaMaxInimigo = 25;
                forca = 3;
                break;
            case BOSS:
                vidaInimigo = 30;
                vidaMaxInimigo = 30;
                forca = 5;
                break;
        }
    }

    public boolean estaVivo() {
        if (vidaInimigo > 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void ataque() {
        sucessoNoDado = false;
        System.out.println("O inimigo tentará atacar");
        System.out.println("Rolando 1D20..");
        dado20.rolar();
        if (dado20.getResultado() >= 15) {
            System.out.println("Rolando 1D6..");
            dado6.rolar();
            resultadoAtaque = dado6.getResultado() + forca;
            sucessoNoDado = true;
        } else {
            System.out.println("O inimigo não teve sucesso.");
            sucessoNoDado = false;
        }
    }

    @Override
    public void cura() {
        sucessoNoDado = false;
        System.out.println("O inimigo tentara se curar");
        if (Objects.equals(vidaInimigo, vidaMaxInimigo)) {
            System.out.println("O inimigo não pode se curar, já está na vida maxima.");
        } else {
            System.out.println("Rolando 1D20..");
            dado20.rolar();
            if (dado20.getResultado() >= 15) {
                System.out.println("Rolando 1D6..");
                dado6.rolar();
                Integer recuperacao = getVidaInimigo() + dado6.getResultado();

                // Lógica com if/else para travar na vida máxima
                if (recuperacao > vidaMaxInimigo) {
                    vidaInimigo = vidaMaxInimigo;
                } else {
                    vidaInimigo = recuperacao;
                }
                sucessoNoDado = true;
            } else {
                System.out.println("O inimigo não teve sucesso.");
                sucessoNoDado = false;
            }
        }
    }

    @Override
    public void defesa() {
        sucessoNoDado = false;
        System.out.println("O inimigo tentará se defender");
        System.out.println("Rolando 1D20..");
        dado20.rolar();
        if (dado20.getResultado() >= 15) {
            System.out.println("Rolando 1D6..");
            dado6.rolar();
            resultadoDefesa = dado6.getResultado();
            sucessoNoDado = true;
        } else {
            System.out.println("O inimigo não teve sucesso.");
            sucessoNoDado = false;
        }
    }

    public int receberDano(int dano) {
        Integer vida = getVidaInimigo();
        setVidaInimigo(vida - dano);
        return getVidaInimigo();
    }

    //Get e Set
    public Integer getVidaInimigo() { return vidaInimigo; }
    public void setVidaInimigo(Integer vidaInimigo) { this.vidaInimigo = vidaInimigo; }
    public Integer getVidaMaxInimigo() { return vidaMaxInimigo; }
    public void setVidaMaxInimigo(Integer vidaMaxInimigo) { this.vidaMaxInimigo = vidaMaxInimigo; }
    public boolean isSucessoNoDado() { return sucessoNoDado; }
    public void setSucessoNoDado(boolean sucessoNoDado) { this.sucessoNoDado = sucessoNoDado; }
    public int getResultadoAtaque() { return resultadoAtaque; }
    public void setResultadoAtaque(int resultadoAtaque) { this.resultadoAtaque = resultadoAtaque; }
    public Integer getResultadoDefesa() { return resultadoDefesa; }
    public void setResultadoDefesa(Integer resultadoDefesa) { this.resultadoDefesa = resultadoDefesa; }
}
