package com.rpg.backend.model;

import java.util.Scanner;

public class Jogador {
    Scanner leia = new Scanner(System.in);
    private String nome;

    public String alterarNome() {
        System.out.println("Digite o seu nome de jogador: ");
        setNome(leia.nextLine());
        return getNome();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}