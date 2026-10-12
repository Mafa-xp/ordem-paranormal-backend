package com.rpg.backend.model;

import com.rpg.backend.exception.PersonagemMortoExeption;
import java.util.Objects;

public class Ocultista extends Personagem {

    Dado dado20 = new Dado(20);
    Dado dado6 = new Dado(6);

    public Ocultista(String tipoPersonagem, Integer vida, Integer vidaMax, Integer forca) {
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
            System.out.println("Boa você tirou " + dado20.getResultado());
            System.out.println("Rolando 1D6..");
            dado6.rolar();
            int danoTotal = dado6.getResultado() + getForca();
            System.out.println("Você dá um Dano no inimigo: " + dado6.getResultado() + " + " + getForca() + " de força");

            setResultadoAtaque(danoTotal);
            setSucessoNoDado(true); // ✅ Sucesso correto
        } else {
            System.out.println("Puts, você tirou " + dado20.getResultado() + " sua ação não deu certo.");
            setResultadoAtaque(0);
            setSucessoNoDado(false); // ✅ Falha correta
        }
    }

    @Override
    public void cura() {
        setSucessoNoDado(false);
        if (Objects.equals(getVida(), getVidaMax())) {
            System.out.println("Você não pode se curar, sua vida já está no maximo.");
        } else {
            System.out.println("Rolando 1D20..");
            dado20.rolar();

            if (dado20.getResultado() >= 15) {
                System.out.println("Boa você tirou " + dado20.getResultado());
                System.out.println("Rolando 1D6..");
                dado6.rolar();

                Integer recuperacao = getVida() + dado6.getResultado();

                if (recuperacao > getVidaMax()) {
                    setVida(getVidaMax());
                } else {
                    setVida(recuperacao);
                }

                System.out.println("Você se curou! Vida atual: " + getVida() + "/" + getVidaMax());
                setSucessoNoDado(true); // ✅ Sucesso correto
            } else {
                System.out.println("Puts, você tirou " + dado20.getResultado() + " sua ação não deu certo.");
                setSucessoNoDado(false); // ✅ Falha correta
            }
        }
    }

    @Override
    public void defesa() {
        setSucessoNoDado(false);
        System.out.println("Rolando 1D20..");
        dado20.rolar();

        if (dado20.getResultado() >= 15) {
            System.out.println("Boa você tirou " + dado20.getResultado());
            System.out.println("Rolando 1D6..");
            dado6.rolar();
            System.out.println("Você dá uma defesa de " + dado6.getResultado());

            setResultadoDefesa(dado6.getResultado());
            setSucessoNoDado(true); // ✅ Sucesso correto
        } else {
            System.out.println("Puts, você tirou " + dado20.getResultado() + " sua ação não deu certo.");
            setResultadoDefesa(0);
            setSucessoNoDado(false); // ✅ Falha correta
        }
    }
}