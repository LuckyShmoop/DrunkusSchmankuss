package cards;

import player.Player;
import java.util.List;
import java.util.Scanner;

public abstract class Card {
    // ... (Felder und Konstruktor bleiben gleich) ...
    private final String title;
    private final String description;
    private final Rarity rarity;
    private final ICardAction action;

    public Card(String title, String description, Rarity rarity, ICardAction action) {
        this.title = title;
        this.description = description;
        this.rarity = rarity;
        this.action = action;
    }

    // Die activate-Methode wird erweitert
    public void activate(Player cardPlayer, List<Player> allPlayers, Scanner scanner) {
        if (action != null) {
            action.execute(cardPlayer, allPlayers, scanner);
        }
    }

    public String getTitle() { return title; }

    @Override
    public String toString() {
        return title + " (" + rarity + "): " + description;
    }

    public void activate(Player player) {
    }
}