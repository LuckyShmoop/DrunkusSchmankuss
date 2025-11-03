package com.drunkus.drunkus_schmankus.cards;

import com.drunkus.drunkus_schmankus.game.ConsoleView; // Wichtig: importieren
import com.drunkus.drunkus_schmankus.player.Player;
import java.util.List;

public interface ICardAction {
    /**
     * Führt die Karten-Logik aus.
     * @param cardPlayer Der Spieler, der die Karte ausspielt.
     * @param allPlayers Eine Liste aller Spieler im Spiel.
     * @param view Die View, um mit dem Benutzer zu interagieren (z.B. Ziele auswählen).
     */
    void execute(Player cardPlayer, List<Player> allPlayers, ConsoleView view);
}