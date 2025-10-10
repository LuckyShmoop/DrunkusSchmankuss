package cards.actions;

import cards.ICardAction;
import player.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DistributeShotsAction implements ICardAction {

    private final int totalAmount;

    public DistributeShotsAction(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public void execute(Player cardPlayer, List<Player> allPlayers, Scanner scanner) {
        // Erstelle eine Liste von möglichen Zielen (alle außer dem Spieler selbst)
        List<Player> targets = new ArrayList<>(allPlayers);
        targets.remove(cardPlayer);

        if (targets.isEmpty()) {
            System.out.println("Keine anderen Spieler da, um Schlucke zu verteilen.");
            return;
        }

        int amountLeft = this.totalAmount;
        System.out.println(cardPlayer.getName() + ", du kannst " + amountLeft + " Schlucke verteilen.");

        while (amountLeft > 0) {
            System.out.println("Noch zu verteilen: " + amountLeft);

            // Ziele anzeigen
            for (int i = 0; i < targets.size(); i++) {
                System.out.println((i + 1) + ": " + targets.get(i).getName());
            }

            // Ziel auswählen
            int targetChoice = -1;
            while (targetChoice < 1 || targetChoice > targets.size()) {
                System.out.print("Wähle einen Spieler (1-" + targets.size() + "): ");
                try {
                    targetChoice = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) { /* Ignorieren und neu versuchen */ }
            }
            Player selectedTarget = targets.get(targetChoice - 1);

            // Menge auswählen
            int amountToGive = 0;
            while (amountToGive <= 0 || amountToGive > amountLeft) {
                System.out.print("Wie viele Schlucke für " + selectedTarget.getName() + " (1-" + amountLeft + "): ");
                try {
                    amountToGive = Integer.parseInt(scanner.nextLine());
                } catch (NumberFormatException e) { /* Ignorieren und neu versuchen */ }
            }

            // Aktion ausführen und Zähler aktualisieren
            System.out.println(selectedTarget.getName() + " erhält " + amountToGive + " Schlucke.");
            selectedTarget.takeSwallow(amountToGive);
            amountLeft -= amountToGive;
        }

        System.out.println("Alle Schlucke wurden verteilt!");
    }
}