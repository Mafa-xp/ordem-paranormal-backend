package com.rpg.backend.service;

import com.rpg.backend.enums.NivelInimigo;
import com.rpg.backend.model.*;

import java.util.Scanner;

public class Menu {
    Scanner leia = new Scanner(System.in);
    Personagem personagem = null;
    Inimigo inimigo = null;

    public void menuInicial() {
        Jogador jogador = new Jogador();
        jogador.alterarNome();

        regras();

        System.out.println("----------------------------");
        System.out.println("Escolha a classe do personagem: ");
        System.out.println("1 - OCULTISTA");
        System.out.println("2 - COMBATENTE");
        System.out.println("3 - ESPECIALISTA");
        int opcao = leia.nextInt();
        System.out.println("----------------------------");

        switch (opcao) {
            case 1:
                personagem = new Ocultista("Ocultista", 20, 20, 2);
                personagem.alterarNome();
                personagem.exibirFicha();
                break;
            case 2:
                personagem = new Combatente("Combatente", 15, 15, 4);
                personagem.alterarNome();
                personagem.exibirFicha();
                break;
            case 3:
                personagem = new Especialista("Especialista", 18, 18, 3);
                personagem.alterarNome();
                personagem.exibirFicha();
                break;
            default:
                System.out.println("Opção inválida");
                break;
        }

        tipoDeInimigo();
        Batalha batalha = new Batalha(personagem, inimigo);
        batalha.iniciar();
    }

    public void tipoDeInimigo() {
        System.out.println("Qual o nivel do seu inimigo: ");
        System.out.println("1 - Facil");
        System.out.println("2 - Médio");
        System.out.println("3 - Boss Final");
        int opcao = leia.nextInt();
        if (opcao == 1) {
            inimigo = new Inimigo(NivelInimigo.FACIL);
        } else if (opcao == 2) {
            inimigo = new Inimigo(NivelInimigo.MEDIO);
        } else {
            inimigo = new Inimigo(NivelInimigo.BOSS);
        }
    }

    void regras() {
        System.out.println("----REGRAS----");
        System.out.println("Só pode alterar o nome do seu personagem uma vez");
        System.out.println("----------------------------");
        System.out.println("Cada classe (Ocultista, Especialista e Combatete) tem seus diferenciais");
        System.out.println("Ocultista: Mais vida e menos força");
    }
}

