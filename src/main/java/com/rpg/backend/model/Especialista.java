package com.rpg.backend.model;

import com.rpg.backend.exception.PersonagemMortoExeption;

import java.util.Objects;

public class Especialista extends Personagem {
    private int intelecto = 3;
    Dado dado20 = new Dado(20);
    Dado dado6 = new Dado(6);

    public Especialista(String tipoPersonagem, Integer vida, Integer vidaMax, Integer forca) {
        super(tipoPersonagem, vida, vidaMax, forca);
    }

    @Override
    public void ataque() throws PersonagemMortoExeption {
        if (!estaVivo()) {
            throw new PersonagemMortoExeption("O personagem está morto e não pode atacar.");
        }
        setSucessoNoDado(false);
        System.out.println("Rolando 1D20..");
        dado20.rolar();

        // O Intelecto ajuda a passar no teste d20
        if (dado20.getResultado() + intelecto >= 15) {
            System.out.println("Boa! Você tirou " + dado20.getResultado() + " + " + intelecto + " (Intelecto)");
            System.out.println("Rolando 1D6..");
            dado6.rolar();

            // Somando o d6 + força + intelecto para o dano do Especialista brilhar!
            int danoTotal = dado6.getResultado() + getForca() + intelecto;
            System.out.println("Ataque do Especialista! Dano causado: " + dado6.getResultado() + " + " + getForca() + " (Força) + " + intelecto + " (Intelecto) = " + danoTotal);

            setResultadoAtaque(danoTotal);
            setSucessoNoDado(true);
        } else {
            System.out.println("Puts, você tirou " + dado20.getResultado() + " + " + intelecto + ". Seu ataque falhou!");
            setSucessoNoDado(false);
        }
    }
    @Override
    public void cura() {
        setSucessoNoDado(false);
        if (Objects.equals(getVida(), getVidaMax())) {
            System.out.println("Você não pode se curar, sua vida já está no máximo.");
        } else {
            System.out.println("Rolando 1D20..");
            dado20.rolar();
            if (dado20.getResultado() + intelecto >= 15) { // Incluído o intelecto aqui também
                System.out.println("Boa! Você tirou " + dado20.getResultado());
                System.out.println("Rolando 1D6..");
                dado6.rolar();

                // Opcional: somar o intelecto na eficácia da cura do especialista
                Integer recuperacao = getVida() + dado6.getResultado() + intelecto;
                if (recuperacao > getVidaMax()) {
                    setVida(getVidaMax());
                } else {
                    setVida(recuperacao);
                }

                System.out.println("Você se curou! Vida atual: " + getVida() + "/" + getVidaMax());
                setSucessoNoDado(true);
            } else {
                System.out.println("Puts, você tirou " + dado20.getResultado() + ". A cura falhou.");
                setSucessoNoDado(false);
            }
        }
    }

    @Override
    public void defesa() {
        setSucessoNoDado(false);
        System.out.println("Rolando 1D20..");
        dado20.rolar();
        if (dado20.getResultado() + intelecto >= 15) { // Incluído o intelecto aqui também
            System.out.println("Boa! Você tirou " + dado20.getResultado());
            System.out.println("Rolando 1D6..");
            dado6.rolar();

            // Opcional: somar intelecto na defesa tática do especialista
            int defesaTotal = dado6.getResultado() + intelecto;
            System.out.println("Defesa do Especialista! Valor defendido: " + defesaTotal);
            setResultadoDefesa(defesaTotal);
            setSucessoNoDado(true);
        } else {
            System.out.println("Puts, você tirou " + dado20.getResultado() + ". A defesa falhou!");
            setSucessoNoDado(false);
        }
    }
}
