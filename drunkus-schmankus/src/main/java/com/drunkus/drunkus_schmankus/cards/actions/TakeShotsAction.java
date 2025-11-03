package com.drunkus.drunkus_schmankus.cards.actions;

import com.drunkus.drunkus_schmankus.game.ConsoleView;
import com.drunkus.drunkus_schmankus.player.Player;
import java.util.List;

public class TakeShotsAction extends AbstractTakeDrinkAction {

    public TakeShotsAction(int amount) {
        super(amount);
    }

    @Override
    protected void applyEffect(Player target, int amount, ConsoleView view) {
        // 1. Zähler hochzählen
        target.takeShot(amount);

        // 2. EXP berechnen und hinzufügen
        int expAmount = amount * 3;
        List<Integer> newLevels = target.addExp(expAmount);

        // 3. Level-Ups anzeigen
        for (int newLvl : newLevels) {
            view.displayMessage(target.getName() + " ist auf LEVEL " + newLvl + " aufgestiegen!");
        }
    }

    @Override
    protected String getDrinkName(int amount) {
        return (amount == 1) ? "Shot" : "Shots";
    }
}