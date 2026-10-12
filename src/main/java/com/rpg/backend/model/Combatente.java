package com.rpg.backend.model;

import com.rpg.backend.exception.PersonagemMortoExeption;
import java.util.Objects;

public class Combatente extends Personagem implements Acao {
    Dado dado20 = new Dado(20);
    Dado dado6 = new Dado(6);

    public Combatente(String tipoPersonagem, Integer vida, Integer vidaMax, Integer forca) {
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
        if (dado20.getResultado() >= 15) {
            System.out.println("Boa! Você tirou " + dado20.getResultado());
            System.out.println("Rolando 1D6..");
            dado6.rolar();
            int danoTotal = dado6.getResultado() + getForca();
            System.out.println("Ataque do Combatente! Dano causado: " + dado6.getResultado() + " + " + getForca() + " de força = " + danoTotal);
            setResultadoAtaque(danoTotal);
            setSucessoNoDado(true);
        } else {
            System.out.println("Puts, você tirou " + dado20.getResultado() + ". Seu ataque falhou!");
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
            if (dado20.getResultado() >= 15) {
                System.out.println("Boa! Você tirou " + dado20.getResultado());
                System.out.println("Rolando 1D6..");
                dado6.rolar();

                Integer recuperacao = getVida() + dado6.getResultado();
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
        if (dado20.getResultado() >= 15) {
            System.out.println("Boa! Você tirou " + dado20.getResultado());
            System.out.println("Rolando 1D6..");
            dado6.rolar();
            System.out.println("Defesa do Combatente! Valor defendido: " + dado6.getResultado());
            setResultadoDefesa(dado6.getResultado());
            setSucessoNoDado(true);
        } else {
            System.out.println("Puts, você tirou " + dado20.getResultado() + ". A defesa falhou!");
            setSucessoNoDado(false);
        }
    }
}