package com.rpg.backend.model;

import com.rpg.backend.exception.PersonagemMortoExeption;

import java.util.Random;
import java.util.Scanner;

public class Batalha {
    Scanner leia = new Scanner(System.in);
    private Personagem player;
    private Inimigo inimigo;

    private static final Integer BONUS_SUPER_ATAQUE = 10;
    private static final Integer TURNOS_CARREGANDO = 5;

    // Contadores para controlar a recarga dos Ataques Especiais
    private int contagemEspecialPlayer = 0;
    private int contagemEspecialInimigo = 0;

    public Batalha(Personagem player, Inimigo inimigo) {
        this.player = player;
        this.inimigo = inimigo;
    }

    public void iniciar() {
        while(this.player.estaVivo() && this.inimigo.estaVivo()) {
            // Diminui a recarga a cada rodada
            if (contagemEspecialPlayer > 0){
                contagemEspecialPlayer--;
            }
            if (contagemEspecialInimigo > 0){
                contagemEspecialInimigo--;
            }

            turnoJogador();
            if (!inimigo.estaVivo()) {
                break;
            }
            turnoInimigo();
        }
        verificaVencedor();
    }

    private void turnoJogador() {
        System.out.println("\n==========================================");
        System.out.println("          TURNO DO JOGADOR          ");
        System.out.println("==========================================");
        System.out.println(" Vida Atual: [" + player.getVida() + "/" + player.getVidaMax() + "]");
        System.out.println("O que quer fazer?");
        System.out.println("1 - Atacar");
        System.out.println("2 - Defender");
        System.out.println("3 - Curar");

        // Exibe o status do Especial
        if (contagemEspecialPlayer == 0) {
            System.out.println("4 - [ESPECIAL PRONTO!] Ataque Devastador");
        } else {
            System.out.println("4 - Ataque Especial (Aguarde " + contagemEspecialPlayer + " turno(s))");
        }
        System.out.println("5 - Abandonar batalha..");

        int opcao = leia.nextInt();
        switch (opcao) {
            case 1:
                try {
                    player.ataque(); // Funciona para QUALQUER personagem!
                    if (player.isSucessoNoDado()) {
                        inimigo.receberDano(player.getResultadoAtaque());
                        player.setResultadoAtaque(0);
                    }
                } catch (PersonagemMortoExeption e) {
                    System.out.println(e.getMessage());
                }
                break;
            case 2:
                player.defesa();
                break;
            case 3:
                player.cura();
                break;
            case 4:
                if (contagemEspecialPlayer == 0) {
                    executarAtaqueEspecialPlayer();
                    contagemEspecialPlayer = TURNOS_CARREGANDO; // Define turnos de recarga
                } else {
                    System.out.println("O Especial ainda não está pronto! Você perdeu a vez!");
                }
                break;
            case 5:
                System.out.println("Você abandonou a batalha..");
                player.setVida(0); // Derrota automática ao fugir
                break;
        }
    }


    private void executarAtaqueEspecialPlayer() {
        System.out.println("----VOCÊ USOU SEU ATAQUE ESPECIAL!----");
        Dado d6 = new Dado(6);
        d6.rolar();
        int danoEspecial = d6.getResultado() + player.getForca() + BONUS_SUPER_ATAQUE; // Dano extra bem mais forte
        System.out.println("Ataque Super causou " + danoEspecial + " de dano!");
        aplicarDanoNoInimigo(danoEspecial);
    }

    private void aplicarDanoNoInimigo(int dano) {
        if (inimigo.getResultadoDefesa() > 0) {
            dano = Math.max(0, dano - inimigo.getResultadoDefesa());
            inimigo.setResultadoDefesa(0);
        }
        inimigo.receberDano(dano);
    }

    private void turnoInimigo() {
        System.out.println("\n==========================================");
        System.out.println("          TURNO DO INIMIGO           ");
        System.out.println("==========================================");
        System.out.println("Vida inimigo: " + inimigo.getVidaInimigo() + "/" + inimigo.getVidaMaxInimigo());

        // Se o Especial do inimigo estiver pronto, ele usa com prioridade
        if (contagemEspecialInimigo == 0) {
            System.out.println(" O INIMIGO CARREGOU UM ATAQUE SUPER!");
            Dado d6 = new Dado(6);
            d6.rolar();
            int danoSuper = d6.getResultado() + 10;
            System.out.println("O inimigo te acertou um golpe devastador de dano!");

            if (player.getResultadoDefesa() > 0) {
                danoSuper = danoSuper - player.getResultadoDefesa();
                if (danoSuper < 0) {
                    danoSuper = 0;
                }
                player.setResultadoDefesa(0);
            }
            player.receberDano(danoSuper);
            contagemEspecialInimigo = 4; // Recarga de 4 turnos para o inimigo
            return;
        }

        // Caso contrário, ele faz a ação padrão
        Random aleatorio = new Random();
        int dadoInimigo = aleatorio.nextInt(3) + 1; // 1: Ataque, 2: Cura, 3: Defesa
        switch (dadoInimigo) {
            case 1:
                inimigo.ataque();
                if (inimigo.isSucessoNoDado()) {
                    int dano = inimigo.getResultadoAtaque();
                    if (player.getResultadoDefesa() > 0) {
                        dano = dano - player.getResultadoDefesa();
                        if (dano < 0) {
                            dano = 0;
                        }
                        player.setResultadoDefesa(0);
                    }
                    player.receberDano(dano);
                    inimigo.setResultadoAtaque(0);
                }
                break;
            case 2:
                inimigo.cura();
                break;
            case 3:
                inimigo.defesa();
                break;
        }
    }

    private void verificaVencedor() {
        if (!player.estaVivo()) {
            System.out.println("O inimigo venceu! Você foi derrotado.");
        } else if (!inimigo.estaVivo()) {
            System.out.println(player.getNome() + " venceu a batalha!!");
        }
    }
}
