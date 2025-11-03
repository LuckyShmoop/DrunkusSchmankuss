package com.drunkus.drunkus_schmankus.cards.actions;

import com.drunkus.drunkus_schmankus.cards.ICardAction;
import com.drunkus.drunkus_schmankus.game.ConsoleView;
import com.drunkus.drunkus_schmankus.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Eine abstrakte Basisklasse für Aktionen, die Getränke (Shots oder Schlucke)
 * an Mitspieler verteilen. Sie implementiert die gesamte Logik der Zielauswahl
 * und der Verteilungsschleife (Template Method Pattern).
 */
public abstract class AbstractDistributeDrinkAction implements ICardAction {

    private final int totalAmount;

    public AbstractDistributeDrinkAction(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Diese 'execute'-Methode ist final und enthält die gemeinsame Logik.
     */
    @Override
    public final void execute(Player cardPlayer, List<Player> allPlayers, ConsoleView view) {
        List<Player> targets = new ArrayList<>(allPlayers);
        targets.remove(cardPlayer);

        if (targets.isEmpty()) {
            view.displayMessage("Keine anderen Spieler da, um " + getDrinkName(totalAmount) + " zu verteilen.");
            return;
        }

        int amountLeft = this.totalAmount;
        view.displayMessage(cardPlayer.getName() + ", du kannst " + amountLeft + " " + getDrinkName(amountLeft) + " verteilen.");

        while (amountLeft > 0) {
            view.displayMessage("Noch zu verteilen: " + amountLeft);

            Player selectedTarget = view.askForPlayerSelection(targets, "Wähle einen Spieler:");
            String prompt = "Wie viele " + getDrinkName(amountLeft) + " für " + selectedTarget.getName() + " (1-" + amountLeft + "): ";
            int amountToGive = view.askForIntegerInRange(prompt, 1, amountLeft);

            view.displayMessage(selectedTarget.getName() + " erhält " + amountToGive + " " + getDrinkName(amountToGive) + ".");

            // Ruft die spezifische Logik der Kind-Klasse auf
            applyEffect(selectedTarget, amountToGive, view);

            amountLeft -= amountToGive;
        }
        view.displayMessage("Alle " + getDrinkName(totalAmount) + " wurden verteilt!");
    }

    /**
     * Kind-Klassen müssen implementieren, was genau passiert, wenn ein
     * Spieler das Getränk erhält (z.B. takeShot vs takeSwallow und EXP-Berechnung).
     */
    protected abstract void applyEffect(Player target, int amount, ConsoleView view);

    /**
     * Kind-Klassen müssen den Namen des Getränks (Singular/Plural) bereitstellen.
     */
    protected abstract String getDrinkName(int amount);
}