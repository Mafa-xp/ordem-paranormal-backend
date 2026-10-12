package com.rpg.backend.model;

import java.util.Scanner;

public abstract class Personagem implements Acao{
    Scanner leia = new Scanner(System.in);

    private String nome;
    private String tipoPersonagem;
    private Integer vida;
    private Integer vidaMax;
    private Integer forca;

    private boolean sucessoNoDado = false;
    private Integer resultadoAtaque = 0;
    private Integer resultadoDefesa = 0;

    public Personagem(String tipoPersonagem, Integer vida, Integer vidaMax, Integer forca) {
        this.tipoPersonagem = tipoPersonagem;
        this.vida = vida;
        this.vidaMax = vidaMax;
        this.forca = forca;
    }

    public String alterarNome() {
        System.out.println("Digite o nome do personagem: ");
        setNome(leia.nextLine());
        return getNome();
    }

    public void exibirFicha() {
        System.out.println("---------------------------------");
        System.out.println("Nome: " + nome);
        System.out.println("Tipo de Personagem: " + tipoPersonagem);
        System.out.println("Vida: " + vida + "/" + vidaMax);
        System.out.println("---------------------------------");
    }

    public boolean estaVivo() {
        if (getVida() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public int receberDano(int dano) {
        Integer vida = getVida();
        setVida(vida - dano);
        return getVida();
    }

    //get e sets

    public String getNome() { return nome; }

    public void setNome(String nome) { this.nome = nome; }

    public String getTipoPersonagem() { return tipoPersonagem; }

    public void setTipoPersonagem(String tipoPersonagem) { this.tipoPersonagem = tipoPersonagem; }

    public Integer getVida() { return vida; }

    public void setVida(Integer vida) { this.vida = vida; }

    public Integer getVidaMax() { return vidaMax; }

    public void setVidaMax(Integer vidaMax) { this.vidaMax = vidaMax; }

    public Integer getForca() { return forca; }

    public void setForca(Integer forca) { this.forca = forca; }

    public boolean isSucessoNoDado() { return sucessoNoDado; }

    public void setSucessoNoDado(boolean sucessoNoDado) { this.sucessoNoDado = sucessoNoDado; }

    public int getResultadoAtaque() { return resultadoAtaque; }

    public void setResultadoAtaque(int resultadoAtaque) { this.resultadoAtaque = resultadoAtaque; }

    public Integer getResultadoDefesa() { return resultadoDefesa; }

    public void setResultadoDefesa(Integer resultadoDefesa) { this.resultadoDefesa = resultadoDefesa; }
}