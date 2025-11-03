package com.drunkus.drunkus_schmankus.cards.actions;

import com.drunkus.drunkus_schmankus.game.ConsoleView;
import com.drunkus.drunkus_schmankus.player.Player;
import java.util.List;

public class TakeSwallowsAction extends AbstractTakeDrinkAction {

    public TakeSwallowsAction(int amount) {
        super(amount);
    }

    @Override
    protected void applyEffect(Player target, int amount, ConsoleView view) {
        // 1. Zähler hochzählen
        target.takeSwallow(amount);

        // 2. EXP berechnen und hinzufügen (1:1)
        List<Integer> newLevels = target.addExp(amount);

        // 3. Level-Ups anzeigen
        for (int newLvl : newLevels) {
            view.displayMessage(target.getName() + " ist auf LEVEL " + newLvl + " aufgestiegen!");
        }
    }

    @Override
    protected String getDrinkName(int amount) {
        return (amount == 1) ? "Schluck" : "Schlucke";
    }
}