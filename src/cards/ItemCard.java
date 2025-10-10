package cards;

import player.Player;

// Eine ItemCard ist eine spezielle Art von Karte, die möglicherweise einen passiven
// oder einen besonderen, einmaligen Effekt hat. Sie wird wie eine ActionCard auf der Hand gehalten.
public class ItemCard extends Card {

    public ItemCard(String title, String description, Rarity rarity, ICardAction action) {
        // Wir rufen den Konstruktor der übergeordneten Card-Klasse mit allen nötigen Informationen auf.
        super(title, description, rarity, action);
    }
}