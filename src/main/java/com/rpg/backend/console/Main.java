package com.rpg.backend.console;

import com.rpg.backend.service.Menu;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();

        System.out.println("------SISTEMA DE RPG DE MESA------");
        System.out.println("---------ORDEM PARANORMAL---------");

        menu.menuInicial();
    }

}
