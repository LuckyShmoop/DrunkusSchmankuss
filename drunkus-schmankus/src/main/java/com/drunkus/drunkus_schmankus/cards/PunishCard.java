package com.drunkus.drunkus_schmankus.cards;

// Eine Strafkarte ist einfach eine Karte, die sofort ausgeführt wird.
// Die Logik dafür liegt im GameController. Die Klasse selbst braucht nichts Besonderes.
public class PunishCard extends Card {

    public PunishCard(String title, String description, Rarity rarity, ICardAction action) {
        super(title, description, rarity, action);
    }
}