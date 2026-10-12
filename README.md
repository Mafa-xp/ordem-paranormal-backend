🧩 Ordem Paranormal RPG — Back-end (Spring Boot)

API REST desenvolvida em Java com Spring Boot para gerenciar a lógica, fichas de personagens, testes de atributos e o motor de combate do sistema de RPG **Ordem Paranormal**.

> 🔗 **Front-end da aplicação:** [ordem-paranormal-frontend](https://github.com/SEU_USUARIO/ordem-paranormal-frontend)

---

## 🛠️ Tecnologias Utilizadas

* **Java 17+**
* **Spring Boot 3** (Spring Web)
* **Maven**
* **Orientação a Objetos** (Herança, Polimorfismo e Encapsulamento)

---

## 🎲 Regras e Mecânicas Implementadas

* **Testes de Ação (1D20):** Validação de sucesso para ações de combate (sucesso $\ge 15$).
* **Rolagem de Impacto (1D6):** Cálculo de dano, cura e escudo com modificadores de classe.
* **Classes Jogáveis:**
  * **Ocultista:** Foco em vida mística e suporte.
  * **Combatente:** Foco em força física e dano direto.
  * **Especialista:** Bônus de Intelecto aplicado aos testes de dados.
* **Turno e Cooldown:** Gerenciamento de cooldown de 5 turnos para Ataques Especiais e tomada de decisão de I.A. para o inimigo.

---

## 🚀 Como Executar o Projeto

```bash
# Clone o repositório
git clone [https://github.com/SEU_USUARIO/ordem-paranormal-backend.git](https://github.com/SEU_USUARIO/ordem-paranormal-backend.git)

# Acesse a pasta do projeto
cd ordem-paranormal-backend

# Execute a aplicação via Maven
./mvnw spring-boot:run
