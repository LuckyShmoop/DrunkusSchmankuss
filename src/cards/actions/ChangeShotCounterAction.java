// cards/actions/ChangeShotCounterAction.java

package cards.actions;

import cards.ICardAction;
import player.Player;

public class ChangeShotCounterAction implements ICardAction {

    // Feld, um den Wert zu speichern, der übergeben wird
    private final int amount;

    // Der Konstruktor nimmt den spezifischen Wert (den Parameter) entgegen!
    public ChangeShotCounterAction(int amount) {
        this.amount = amount;
    }

    @Override
    public void execute(Player target) {
        if (amount > 0) {
            System.out.println(target.getName() + " muss " + amount + " Shot(s) nehmen.");
            target.takeShot(amount);
        } else if (amount < 0) {
            // Könnte eine 'Heal'-Karte sein, die Shots reduziert
            System.out.println(target.getName() + " reduziert seinen Shot-Zähler um " + (-amount) + "!");
            target.takeShot(amount); // takeShot funktioniert, da es +/- Werte annimmt
        }
    }
}