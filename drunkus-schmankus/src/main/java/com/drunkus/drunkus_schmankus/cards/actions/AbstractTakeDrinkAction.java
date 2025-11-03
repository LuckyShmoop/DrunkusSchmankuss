package com.drunkus.drunkus_schmankus.cards.actions;

import com.drunkus.drunkus_schmankus.cards.ICardAction;
import com.drunkus.drunkus_schmankus.game.ConsoleView;
import com.drunkus.drunkus_schmankus.player.Player;

import java.util.List;

/**
 * Eine abstrakte Basisklasse für Aktionen, bei denen der Spieler, der die Karte zieht,
 * selbst trinken muss (z.B. bei Strafkarten).
 * Sie implementiert die Template-Methode 'execute'.
 */
public abstract class AbstractTakeDrinkAction implements ICardAction {

    protected final int amount;

    public AbstractTakeDrinkAction(int amount) {
        this.amount = amount;
    }

    /**
     * Diese 'execute'-Methode ist final und enthält die gemeinsame Logik.
     */
    @Override
    public final void execute(Player cardPlayer, List<Player> allPlayers, ConsoleView view) {
        // 1. Hole den Namen des Getränks (wird von Kind-Klasse definiert)
        String drinkName = getDrinkName(this.amount);
        view.displayMessage(cardPlayer.getName() + " muss " + this.amount + " " + drinkName + " nehmen.");

        // 2. Wende den spezifischen Effekt an (wird von Kind-Klasse definiert)
        //    (Diese Methode kümmert sich um takeShot/takeSwallow UND die EXP)
        applyEffect(cardPlayer, this.amount, view);
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