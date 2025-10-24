package cards.actions;

import cards.ICardAction;
import game.ConsoleView;
import player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Eine interaktive Aktion, die den Spieler eine feste Anzahl an Shots
 * auf einen oder mehrere Mitspieler verteilen lässt.
 */
public class DistributeShotsAction implements ICardAction {

    private final int totalAmount;

    public DistributeShotsAction(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public void execute(Player cardPlayer, List<Player> allPlayers, ConsoleView view) {
        List<Player> targets = new ArrayList<>(allPlayers);
        targets.remove(cardPlayer);

        if (targets.isEmpty()) {
            view.displayMessage("Keine anderen Spieler da, um Shots zu verteilen.");
            return;
        }

        int amountLeft = this.totalAmount;
        view.displayMessage(cardPlayer.getName() + ", du kannst " + amountLeft + " Shot(s) verteilen.");

        while (amountLeft > 0) {
            view.displayMessage("Noch zu verteilen: " + amountLeft);

            // Die View kümmert sich um die Auswahl des Spielers
            Player selectedTarget = view.askForPlayerSelection(targets, "Wähle einen Spieler:");

            // Die View kümmert sich um die Auswahl der Menge
            String prompt = "Wie viele Shots für " + selectedTarget.getName() + " (1-" + amountLeft + "): ";
            int amountToGive = view.askForIntegerInRange(prompt, 1, amountLeft);

            view.displayMessage(selectedTarget.getName() + " erhält " + amountToGive + " Shot(s).");
            // Hier wird die korrekte Methode für Shots aufgerufen
            selectedTarget.takeShot(amountToGive);
            amountLeft -= amountToGive;
        }
        view.displayMessage("Alle Shots wurden verteilt!");
    }
}