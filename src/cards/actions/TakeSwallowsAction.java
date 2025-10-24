package cards.actions;

import cards.ICardAction;
import game.ConsoleView;
import player.Player;
import java.util.List;
import java.util.Scanner;

/**
 * Eine simple Aktion, die den ausführenden Spieler eine feste Anzahl an Schlucken nehmen lässt.
 * Sie ist nicht-interaktiv und ignoriert die Liste der anderen Spieler.
 */
public class TakeSwallowsAction implements ICardAction {

    private final int amount;

    public TakeSwallowsAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void execute(Player cardPlayer, List<Player> allPlayers, ConsoleView view) {
        // Die View wird hier nicht gebraucht, aber die Methode muss sie entgegennehmen.
        view.displayMessage(cardPlayer.getName() + " muss " + this.amount + " Schluck(e) nehmen.");
        cardPlayer.takeSwallow(this.amount);
    }
}