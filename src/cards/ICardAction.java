package cards;

import player.Player;
import java.util.List;
import java.util.Scanner;

public interface ICardAction {

    /**
     * Führt die Karten-Logik aus.
     * @param cardPlayer Der Spieler, der die Karte ausspielt.
     * @param allPlayers Eine Liste aller Spieler im Spiel, um Ziele auswählen zu können.
     * @param scanner Ein Scanner-Objekt, um die Benutzereingabe für die Zielauswahl zu lesen.
     */
    void execute(Player cardPlayer, List<Player> allPlayers, Scanner scanner);
}