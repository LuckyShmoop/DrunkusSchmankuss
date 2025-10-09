package cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {

    private final List<Card> cards = new ArrayList<>();
    private final Random random = new Random();

    public Deck() {
        // Beim Erstellen des Decks Karten hinzufügen (Beispiele)
        initializeDeck();
    }

    private void initializeDeck() {
        // Beispiel-Karten für verschiedene Kategorien
        cards.add(new Card("Kleiner Schluck", "Glück", "Du musst einen kleinen Schluck nehmen."));
        cards.add(new Card("Großer Shot", "Pech", "Du musst 2 Shots nehmen."));
        cards.add(new Card("Kettenreaktion", "Challenge", "Der linke Nachbar nimmt einen Shot."));
        cards.add(new Card("Freilos", "Glück", "Setze eine Runde aus."));
        // Füge hier weitere Karten hinzu, um das Deck zu füllen
    }

    // Zieht eine zufällige Karte aus dem Deck
    public static Card drawCard() {
        if (cards.isEmpty()) {
            System.out.println("Deck ist leer! Mische neu...");
            initializeDeck(); // Deck neu befüllen, falls leer
        }

        int index = random.nextInt(cards.size());

        // Die Karte wird gezogen und aus dem Deck entfernt (optional, wenn Karten nur einmal vorkommen sollen)
        // Card drawnCard = cards.remove(index);

        // Wenn Karten wieder ins Deck sollen (wie beim Mischen), die Karte nicht entfernen:
        Card drawnCard = cards.get(index);

        return drawnCard;
    }
}
