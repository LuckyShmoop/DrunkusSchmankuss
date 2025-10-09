package player;

import cards.Card;

import java.util.List;

// 1. Klassenname in PascalCase
public abstract class Player {
    private String name;
    private int swallowsTakenCounter = 0;
    private int shotsTakenCounter = 0;
    private final java.util.List<Card> hand = new java.util.ArrayList<>();
    // Konstruktor
    public Player(String name) {
        this.name = name;
    }

    // Setter-Methoden
    public void setName(String name) {
        this.name = name;
    }
    public void setShotsTakenCounter(int shotsTakenCounter) {
        this.shotsTakenCounter = shotsTakenCounter;
    }
    public void setSwallowsTakenCounter(int swallowsTakenCounter) {
        this.swallowsTakenCounter = swallowsTakenCounter;
    }


    // Getter-Methoden
    public String getName() {
        return name;
    }
    public int getShotsTakenCounter() {
        return shotsTakenCounter;
    }
    public int getSwallowsTakenCounter() {
        return swallowsTakenCounter;
    }


    // Zähler-Methoden
    public void takeShot(int amountOfShots) {
        this.shotsTakenCounter += amountOfShots;
    }
    public void takeSwallow(int amountOfSwallows) {
        this.swallowsTakenCounter += amountOfSwallows;
    }

    // Neue Methode, die der GameController aufruft
    public void addCardToHand(Card card) {
        this.hand.add(card);
    }

    // Neue Methode, um die Hand für Phase 2 anzuzeigen
    public List<Card> getHand() {
        return hand;
    }

    // ... und eine Methode, um eine Karte aus der Hand zu spielen und zu entfernen
    public void playCard(Card card) {
        if (this.hand.remove(card)) {
            card.activate(this);
            // Optional: Karte zum Ablagestapel des Decks hinzufügen
        }
    }
}