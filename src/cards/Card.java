package cards;

import game.ConsoleView; // Wichtig: Den richtigen Typ importieren
import player.Player;
import java.util.List;

/**
 * Die abstrakte Basisklasse für alle Karten im Spiel.
 */
public abstract class Card {

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

    /**
     * Führt die Aktion der Karte aus. Diese Methode akzeptiert jetzt die ConsoleView.
     * @param cardPlayer Der Spieler, der die Karte aktiviert.
     * @param allPlayers Eine Liste aller Spieler im Spiel.
     * @param view Die View, um mit dem Benutzer zu interagieren.
     */
    public void activate(Player cardPlayer, List<Player> allPlayers, ConsoleView view) {
        if (action != null) {
            // Die View wird jetzt korrekt an die Action weitergegeben.
            action.execute(cardPlayer, allPlayers, view);
        }
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return title + " (" + rarity + "): " + description;
    }
}