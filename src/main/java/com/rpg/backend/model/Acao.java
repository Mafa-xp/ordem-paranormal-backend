package com.rpg.backend.model;

import com.rpg.backend.exception.PersonagemMortoExeption;

public interface Acao {
    void ataque() throws PersonagemMortoExeption;
    void cura();
    void defesa();
}
