package com.rpg.backend.controller;

import com.rpg.backend.enums.NivelInimigo;
import com.rpg.backend.exception.PersonagemMortoExeption;
import com.rpg.backend.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/api/jogo")
@CrossOrigin(origins = "*") // Permite que o React fale com o Java sem problemas de segurança
public class GameController {

    private String nomeJogador;
    private Personagem personagem;
    private Inimigo inimigo;
    private Batalha batalha;

    // 1. Recebe o nome do Jogador
    @PostMapping("/jogador")
    public ResponseEntity<Map<String, Object>> definirJogador(@RequestBody Map<String, String> payload) {
        this.nomeJogador = payload.get("nome");
        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", "sucesso");
        resposta.put("mensagem", "Jogador " + nomeJogador + " registado com sucesso!");
        return ResponseEntity.ok(resposta);
    }

    // 2. Escolhe a Classe do Personagem (1: Ocultista, 2: Combatente, 3: Especialista)
    @PostMapping("/classe")
    public ResponseEntity<Map<String, Object>> escolherClasse(@RequestBody Map<String, Integer> payload) {
        int opcao = payload.get("opcao");
        String nomeClasse;

        switch (opcao) {
            case 1:
                this.personagem = new Ocultista("Ocultista", 20, 20, 2);
                nomeClasse = "Ocultista";
                break;
            case 2:
                this.personagem = new Combatente("Combatente", 15, 15, 4);
                nomeClasse = "Combatente";
                break;
            case 3:
                this.personagem = new Especialista("Especialista", 18, 18, 3);
                nomeClasse = "Especialista";
                break;
            default:
                return ResponseEntity.badRequest().body(Map.of("erro", "Classe inválida!"));
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", "sucesso");
        resposta.put("classe", nomeClasse);
        resposta.put("vidaMax", personagem.getVidaMax());
        resposta.put("forca", personagem.getForca());
        return ResponseEntity.ok(resposta);
    }

    // 3. Define o Nome do Personagem
    @PostMapping("/personagem/nome")
    public ResponseEntity<Map<String, Object>> definirNomePersonagem(@RequestBody Map<String, String> payload) {
        String nomePersonagem = payload.get("nome");
        this.personagem.setNome(nomePersonagem);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", "sucesso");
        resposta.put("nomePersonagem", personagem.getNome());
        resposta.put("ficha", "Vida: " + personagem.getVida() + ", Força: " + personagem.getForca());
        return ResponseEntity.ok(resposta);
    }

    // 4. Escolhe o Inimigo (1: Fácil, 2: Médio, 3: Boss) e Inicia a Batalha
    @PostMapping("/inimigo")
    public ResponseEntity<Map<String, Object>> escolherInimigo(@RequestBody Map<String, Integer> payload) {
        int nivel = payload.get("nivel");

        if (nivel == 1) {
            this.inimigo = new Inimigo(NivelInimigo.FACIL);
        } else if (nivel == 2) {
            this.inimigo = new Inimigo(NivelInimigo.MEDIO);
        } else {
            this.inimigo = new Inimigo(NivelInimigo.BOSS);
        }

        this.batalha = new Batalha(this.personagem, this.inimigo);

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", "sucesso");
        resposta.put("mensagem", "Inimigo criado! A batalha vai começar.");
        resposta.put("vidaInimigo", inimigo.getVidaInimigo());
        return ResponseEntity.ok(resposta);
    }

    // 5. Processa o turno de combate (Ação: "atacar" ou "habilidade")
    // Variável para controlar a recarga do especial do inimigo no Java
    private int contagemEspecialInimigo = 0;

    @PostMapping("/acao-batalha")
    public ResponseEntity<Map<String, Object>> realizarAcaoBatalha(@RequestBody Map<String, Object> payload) throws PersonagemMortoExeption {
        String acao = (String) payload.get("acao"); // "atacar", "defender", "curar", "especial"

        if (this.personagem == null || this.inimigo == null) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Batalha não iniciada!"));
        }

        StringBuilder logJogador = new StringBuilder();
        StringBuilder logInimigo = new StringBuilder();

        // Decrementa recarga do especial do inimigo no início da rodada
        if (contagemEspecialInimigo > 0) {
            contagemEspecialInimigo--;
        }

        // ==========================================
        // 1. TURNO DO JOGADOR
        // ==========================================
        switch (acao.toLowerCase()) {
            case "atacar":
                this.personagem.ataque(); // Executa o ataque (seja Combatente, Ocultista ou Especialista)

                if (this.personagem.isSucessoNoDado()) {
                    // Pega o dano real que já foi calculado e guardado dentro da classe do personagem!
                    int dano = this.personagem.getResultadoAtaque();

                    // Aplica o dano no inimigo
                    this.inimigo.receberDano(dano);

                    logJogador.append("🎯 ").append(this.personagem.getNome())
                            .append(" Sucesso no D20 e causou ")
                            .append(dano).append(" de dano!");

                    // Reseta o resultado para o próximo turno
                    this.personagem.setResultadoAtaque(0);
                } else {
                    logJogador.append("❌ ").append(this.personagem.getNome())
                            .append(" Falhou o ataque no D20!");
                }
                break;

            case "curar":
                this.personagem.cura();
                if (this.personagem.isSucessoNoDado()) {
                    logJogador.append("✨ ").append(this.personagem.getNome())
                            .append(" Sucesso no D20 e recuperou vida!");
                } else {
                    logJogador.append("❌ ").append(this.personagem.getNome())
                            .append(" Falhou no D20 e não conseguiu se curar!");
                }
                break;

            case "defender":
                this.personagem.defesa();
                if (this.personagem.isSucessoNoDado()) {
                    logJogador.append("🛡️ ").append(this.personagem.getNome())
                            .append(" Passou no D20 e preparou o escudo!");
                } else {
                    logJogador.append("❌ ").append(this.personagem.getNome())
                            .append(" Falhou na postura de defesa!");
                }
                break;

            case "especial":
                Dado d6 = new Dado(6);
                d6.rolar();
                int danoEspecial = d6.getResultado() + this.personagem.getForca() + 10;
                if (this.inimigo.getResultadoDefesa() > 0) {
                    danoEspecial = Math.max(0, danoEspecial - this.inimigo.getResultadoDefesa());
                    this.inimigo.setResultadoDefesa(0);
                }
                this.inimigo.receberDano(danoEspecial);
                logJogador.append("🔥 ").append(this.personagem.getNome())
                        .append(" usou ATAQUE ESPECIAL causando ").append(danoEspecial).append(" de dano devastador!");
                break;
        }

        // ==========================================
        // 2. TURNO DO INIMIGO (Se continuar vivo)
        // ==========================================
        if (this.inimigo.estaVivo()) {

            // REGRA: Se o Especial do inimigo estiver pronto (contagem = 0), ele USA COM PRIORIDADE!
            if (contagemEspecialInimigo == 0) {
                Dado d6Super = new Dado(6);
                d6Super.rolar();
                int danoSuper = d6Super.getResultado() + 10;

                if (this.personagem.getResultadoDefesa() > 0) {
                    danoSuper = Math.max(0, danoSuper - this.personagem.getResultadoDefesa());
                    this.personagem.setResultadoDefesa(0);
                }

                this.personagem.receberDano(danoSuper);
                contagemEspecialInimigo = 4; // Entra em recarga por 4 turnos
                logInimigo.append(" ⚠️ O INIMIGO USOU UM ATAQUE SUPER DEVASTADOR de ").append(danoSuper).append(" de dano!");

            } else {
                // Caso contrário, ele sorteia entre 1: Ataque, 2: Cura, 3: Defesa
                Random aleatorio = new Random();
                int acaoInimigo = aleatorio.nextInt(3) + 1;

                if (acaoInimigo == 1) {
                    this.inimigo.ataque();
                    if (this.inimigo.isSucessoNoDado()) {
                        int danoInimigo = this.inimigo.getResultadoAtaque();
                        if (this.personagem.getResultadoDefesa() > 0) {
                            danoInimigo = Math.max(0, danoInimigo - this.personagem.getResultadoDefesa());
                            this.personagem.setResultadoDefesa(0);
                        }
                        this.personagem.receberDano(danoInimigo);
                        this.inimigo.setResultadoAtaque(0);
                        logInimigo.append(" 💥 O Inimigo passou no D20 e atacou causando ").append(danoInimigo).append(" de dano!");
                    } else {
                        logInimigo.append(" 🛡️ O Inimigo tentou atacar, mas falhou no D20.");
                    }

                } else if (acaoInimigo == 2) {
                    if (Objects.equals(this.inimigo.getVidaInimigo(), this.inimigo.getVidaMaxInimigo())) {
                        logInimigo.append(" 💤 O Inimigo tentou se curar, mas já está com a vida máxima!");
                    } else {
                        this.inimigo.cura();
                        if (this.inimigo.isSucessoNoDado()) {
                            logInimigo.append(" ✨ O Inimigo passou no D20 e se curou!");
                        } else {
                            logInimigo.append(" ❌ O Inimigo tentou se curar, mas falhou no D20.");
                        }
                    }

                } else if (acaoInimigo == 3) {
                    this.inimigo.defesa();
                    if (this.inimigo.isSucessoNoDado()) {
                        logInimigo.append(" 🛡️ O Inimigo passou no D20 e levantou guarda!");
                    } else {
                        logInimigo.append(" ❌ O Inimigo tentou se defender, mas falhou no teste.");
                    }
                }
            }
        } else {
            logInimigo.append(" ☠️ A ameaça foi eliminada!");
        }

        Map<String, Object> resposta = new HashMap<>();
        resposta.put("status", "sucesso");
        resposta.put("logJogador", logJogador.toString());
        resposta.put("logInimigo", logInimigo.toString());
        resposta.put("vidaJogador", this.personagem.getVida());
        resposta.put("vidaInimigo", this.inimigo.getVidaInimigo());

        return ResponseEntity.ok(resposta);
    }

    // Endpoint para reiniciar/limpar o personagem e voltar a escolher a classe
    @PostMapping("/reiniciar")
    public ResponseEntity<Map<String, String>> reiniciarJogo() {
        this.personagem = null;
        this.inimigo = null;
        Map<String, String> resposta = new HashMap<>();
        resposta.put("status", "sucesso");
        resposta.put("mensagem", "Jogo reiniciado com sucesso!");
        return ResponseEntity.ok(resposta);
    }
}
