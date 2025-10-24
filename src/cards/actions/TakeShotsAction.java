package cards.actions;

import cards.ICardAction;
import game.ConsoleView; // Wichtig: Den richtigen Typ importieren
import player.Player;
import java.util.List;

/**
 * Eine simple Aktion, die den ausführenden Spieler eine feste Anzahl an Shots nehmen lässt.
 * Sie ist nicht-interaktiv.
 */
public class TakeShotsAction implements ICardAction {

    private final int amount;

    public TakeShotsAction(int amount) {
        this.amount = amount;
    }

    /**
     * Diese Methode implementiert jetzt die korrekte Signatur aus ICardAction.
     * @param cardPlayer Der Spieler, der die Karte ausspielt.
     * @param allPlayers Die Liste aller Spieler (wird hier ignoriert).
     * @param view Die View, um Nachrichten auszugeben.
     */
    @Override
    public void execute(Player cardPlayer, List<Player> allPlayers, ConsoleView view) {
        // Die View wird jetzt korrekt für die Ausgabe verwendet.
        view.displayMessage(cardPlayer.getName() + " muss " + this.amount + " Shot(s) nehmen.");
        cardPlayer.takeShot(this.amount);
    }
}