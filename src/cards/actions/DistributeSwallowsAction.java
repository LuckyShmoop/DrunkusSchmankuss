package cards.actions;

import cards.ICardAction;
import game.ConsoleView;
import player.Player;
import java.util.ArrayList;
import java.util.List;

public class DistributeSwallowsAction implements ICardAction {
    private final int totalAmount;

    public DistributeSwallowsAction(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public void execute(Player cardPlayer, List<Player> allPlayers, ConsoleView view) {
        List<Player> targets = new ArrayList<>(allPlayers);
        targets.remove(cardPlayer);

        if (targets.isEmpty()) {
            view.displayMessage("Keine anderen Spieler da, um Schlucke zu verteilen.");
            return;
        }

        int amountLeft = this.totalAmount;
        view.displayMessage(cardPlayer.getName() + ", du kannst " + amountLeft + " Schlucke verteilen.");

        while (amountLeft > 0) {
            view.displayMessage("Noch zu verteilen: " + amountLeft);

            // Die Logik zur Spielerauswahl ist jetzt in der View!
            Player selectedTarget = view.askForPlayerSelection(targets, "Wähle einen Spieler:");

            // Menge auswählen
            String prompt = "Wie viele Schlucke für " + selectedTarget.getName() + " (1-" + amountLeft + "): ";
            int amountToGive = view.askForIntegerInRange(prompt, 1, amountLeft);

            view.displayMessage(selectedTarget.getName() + " erhält " + amountToGive + " Schlucke.");
            selectedTarget.takeSwallow(amountToGive);
            amountLeft -= amountToGive;
        }
        view.displayMessage("Alle Schlucke wurden verteilt!");
    }
}