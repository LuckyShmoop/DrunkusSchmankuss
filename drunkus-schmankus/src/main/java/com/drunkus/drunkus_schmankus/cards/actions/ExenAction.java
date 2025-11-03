package com.drunkus.drunkus_schmankus.cards.actions;

import com.drunkus.drunkus_schmankus.cards.ICardAction;
import com.drunkus.drunkus_schmankus.game.ConsoleView;
import com.drunkus.drunkus_schmankus.player.Player;
import java.util.List;

/**
 * Eine Straf-Aktion, die den Spieler, der die Karte zieht,
 * sein Getränk exen lässt und EXP basierend auf dem Füllstand gibt.
 */
public class ExenAction implements ICardAction {

    @Override
    public void execute(Player cardPlayer, List<Player> allPlayers, ConsoleView view) {
        view.displayMessage(cardPlayer.getName() + " muss sein Getränk exen!");

        // 1. Füllstand abfragen (0-100%)
        int fillPercentage = view.askForIntegerInRange(
                ">>> [Info für " + cardPlayer.getName() + "] Wie voll ist dein Glas in Prozent? (0-100): ", 0, 100);

        // 2. EXP berechnen
        int expAmount = (int) (Player.BASE_EXP_FOR_EXEN * (fillPercentage / 100.0));
        view.displayMessage(cardPlayer.getName() + " erhält " + expAmount + " EXP!");

        // 3. EXP hinzufügen und Level-Up prüfen
        List<Integer> newLevels = cardPlayer.addExp(expAmount);
        for (int newLvl : newLevels) {
            view.displayMessage(cardPlayer.getName() + " ist auf LEVEL " + newLvl + " aufgestiegen!");
        }
    }
}