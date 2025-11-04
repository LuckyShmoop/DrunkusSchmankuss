package com.drunkus.drunkus_schmankus.player;

import com.drunkus.drunkus_schmankus.cards.Card;
import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private final String sessionId;
    private String name;
    private int swallowsTakenCounter = 0;
    private int shotsTakenCounter = 0;
    private final List<Card> hand = new ArrayList<>();


    // --- NEUE LEVEL-LOGIK ---
    private int level = 0;
    private int totalExpSchluecke = 0;

    // Speichert die GESAMT-EXP, die zum Erreichen des Levels benötigt wird.
    private static final int[] SCHLUECKE_FOR_LEVEL = {
            5,   // Level 1
            10,  // Level 2
            16,  // Level 3
            22,  // Level 4
            29,  // Level 5
            36,  // Level 6
            43,  // Level 7
            51,  // Level 8
            59,  // Level 9
            68   // Level 10
    };
    public static final int MAX_LEVEL = SCHLUECKE_FOR_LEVEL.length;
    public static final int BASE_EXP_FOR_EXEN = 20; // Ein volles Glas = 20 Schlucke EXP
    // -------------------------

    public Player(String name, String sessionId) {
        this.name = name;
        this.sessionId = sessionId;
    }


    // --- Zähler-Methoden ---
    public void takeShot(int amountOfShots) {
        this.shotsTakenCounter += amountOfShots;
    }
    public void takeSwallow(int amountOfSwallows) {
        this.swallowsTakenCounter += amountOfSwallows;
    }

    // --- NEUE EXP-METHODE ---
    /**
     * Fügt dem Spieler EXP hinzu und prüft auf Level-Ups.
     * @param schlueckeAmount Die Anzahl der Schlucke, die als EXP hinzugefügt werden.
     * @return Eine Liste der neuen Level, die erreicht wurden (kann leer sein).
     */
    public List<Integer> addExp(int schlueckeAmount) {
        List<Integer> newLevelsAchieved = new ArrayList<>();

        if (this.level >= MAX_LEVEL) {
            return newLevelsAchieved; // Keine EXP mehr bei Max-Level
        }

        this.totalExpSchluecke += schlueckeAmount;

        // Prüfen, ob ein oder mehrere Level-Ups stattgefunden haben
        while (this.level < MAX_LEVEL &&
                this.totalExpSchluecke >= SCHLUECKE_FOR_LEVEL[this.level]) {

            this.level++;
            newLevelsAchieved.add(this.level);
        }

        return newLevelsAchieved;
    }

    // --- Getter-Methoden ---
    public String getName() { return name; }
    public int getShotsTakenCounter() { return shotsTakenCounter; }
    public int getSwallowsTakenCounter() { return swallowsTakenCounter; }
    public List<Card> getHand() { return hand; }
    public int getLevel() { return this.level; } // Neuer Getter
    public String getSessionId() { return sessionId; }

    // --- Hand-Methoden ---
    public void addCardToHand(Card card) {
        this.hand.add(card);
    }


}