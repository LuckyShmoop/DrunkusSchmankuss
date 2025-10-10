package cards;

// Eine Aktionskarte ist eine Karte, die man auf der Hand halten kann.
public class ActionCard extends Card {

    public ActionCard(String title, String description, Rarity rarity, ICardAction action) {
        super(title, description, rarity, action);
    }
}