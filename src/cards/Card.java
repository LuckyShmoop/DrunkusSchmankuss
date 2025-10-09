package cards;

import player.Player;
import cards.Rarity;

public class Card {

    private final String title;
    private final String category;
    private final String description;
    private final Rarity rarity;

    public Card(String title, String category, String description) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.rarity = rarity;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public void activate(Player player) {

    }


}
