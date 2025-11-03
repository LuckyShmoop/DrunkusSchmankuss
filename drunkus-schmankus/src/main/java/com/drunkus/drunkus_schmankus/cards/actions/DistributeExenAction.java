package com.drunkus.drunkus_schmankus.cards.actions;

import com.drunkus.drunkus_schmankus.cards.ICardAction;
import com.drunkus.drunkus_schmankus.game.ConsoleView;
import com.drunkus.drunkus_schmankus.player.Player;
import java.util.ArrayList;
import java.util.List;

/**
 * Eine interaktive Aktion, die den Spieler einen Mitspieler
 * auswählen lässt, der sein Getränk exen muss.
 */
public class DistributeExenAction implements ICardAction {

    @Override
    public void execute(Player cardPlayer, List<Player> allPlayers, ConsoleView view) {
        List<Player> targets = new ArrayList<>(allPlayers);
        targets.remove(cardPlayer);

        if (targets.isEmpty()) {
            view.displayMessage("Keine anderen Spieler da, um zum Exen zu zwingen.");
            return;
        }

        // 1. Ziel auswählen
        Player target = view.askForPlayerSelection(targets, "Wen willst du zum Exen zwingen?");
        view.displayMessage(target.getName() + " muss auf Befehl von " + cardPlayer.getName() + " exen!");

        // 2. Füllstand des Ziels abfragen
        int fillPercentage = view.askForIntegerInRange(
                ">>> [Info für " + target.getName() + "] Wie voll ist dein Glas in Prozent? (0-100): ", 0, 100);

        // 3. EXP berechnen
        int expAmount = (int) (Player.BASE_EXP_FOR_EXEN * (fillPercentage / 100.0));
        view.displayMessage(target.getName() + " erhält " + expAmount + " EXP!");

        // 4. EXP beim Ziel hinzufügen und Level-Up prüfen
        List<Integer> newLevels = target.addExp(expAmount);
        for (int newLvl : newLevels) {
            view.displayMessage(target.getName() + " ist auf LEVEL " + newLvl + " aufgestiegen!");
        }
    }
}