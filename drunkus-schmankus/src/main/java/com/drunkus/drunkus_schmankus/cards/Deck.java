package com.drunkus.drunkus_schmankus.cards;

import com.drunkus.drunkus_schmankus.cards.actions.DistributeShotsAction;
import com.drunkus.drunkus_schmankus.cards.actions.DistributeSwallowsAction;
import com.drunkus.drunkus_schmankus.cards.actions.TakeShotsAction;
import com.drunkus.drunkus_schmankus.cards.actions.TakeSwallowsAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Deck {

    // 1. Definiere die Gesamtgröße des Decks
    private static final int TOTAL_DECK_SIZE = 50; // Du kannst diese Zahl jederzeit ändern

    private final List<Card> cards = new ArrayList<>();
    private final List<Card> discardPile = new ArrayList<>();
    private final Random random = new Random();

    // 2. Master-Listen für alle verfügbaren Karten, nach Seltenheit sortiert
    private final List<Card> allCommonCards = new ArrayList<>();
    private final List<Card> allUncommonCards = new ArrayList<>();
    private final List<Card> allRareCards = new ArrayList<>();
    private final List<Card> allSuperRareCards = new ArrayList<>();

    public Deck() {
        // Zuerst alle möglichen Karten in die Master-Listen laden
        loadAllPossibleCards();
        // Dann das Deck basierend auf den Prozenten zusammenstellen
        initializeDeck();
        shuffle();
    }

    /**
     * Füllt die Master-Listen mit JEDER Karte, die im Spiel existiert.
     */
    /**
     * Füllt die Master-Listen mit JEDER Karte, die im Spiel existiert.
     */
    /**
     * Füllt die Master-Listen mit JEDER Karte, die im Spiel existiert.
     */
    private void loadAllPossibleCards() {
        // --- COMMON KARTEN (40%) ---
        // KORRIGIERT: Verwendet jetzt TakeSwallowsAction
        allCommonCards.add(new PunishCard("Stolperstein", "Du nimmst 2 Schlucke.", Rarity.COMMON, new TakeSwallowsAction(2)));
        allCommonCards.add(new PunishCard("Kurzer Aussetzer", "Nimm 3 Schlucke.", Rarity.COMMON, new TakeSwallowsAction(3)));

        // ActionCards bleiben interaktiv (das ist korrekt)
        allCommonCards.add(new ActionCard("Prost!", "Verteile einen Schluck an einen Mitspieler.", Rarity.COMMON, new DistributeSwallowsAction(1)));
        allCommonCards.add(new ActionCard("Wasser marsch!", "Verteile 2 Schlucke an einen Mitspieler.", Rarity.COMMON, new DistributeSwallowsAction(2)));

        // --- UNCOMMON KARTEN (25%) ---
        // KORRIGIERT: Verwendet jetzt TakeSwallowsAction
        allUncommonCards.add(new PunishCard("Falsch abgebogen", "Nimm 5 Schlucke.", Rarity.UNCOMMON, new TakeSwallowsAction(5)));

        allUncommonCards.add(new ActionCard("Eine Runde Mitleid", "Verteile 3 Schlucke an einen Mitspieler.", Rarity.UNCOMMON, new DistributeSwallowsAction(3)));
        allUncommonCards.add(new ActionCard("Zielwasser", "Verteile einen Shot an einen Mitspieler.", Rarity.UNCOMMON, new DistributeShotsAction(1)));

        // --- RARE KARTEN (20%) ---
        // KORRIGIERT: Verwendet jetzt TakeShotsAction
        allRareCards.add(new PunishCard("Donnerschlag", "Du musst sofort einen Shot nehmen.", Rarity.RARE, new TakeShotsAction(1)));

        allRareCards.add(new PunishCard("So eine Schande", "Nimm einen Shot und 2 Schlucke.", Rarity.RARE, null)); // TODO: Needs a combined action
        allRareCards.add(new ActionCard("Doppelt oder nichts", "Verteile 2 Shots an einen Spieler.", Rarity.RARE, new DistributeShotsAction(2)));
        allRareCards.add(new ActionCard("Spiegel", "Der nächste Spieler, der dir etwas zu trinken gibt, muss es selbst nehmen.", Rarity.RARE, null)); // TODO: Needs special logic

        // --- SUPER RARE KARTEN (15%) ---
        allSuperRareCards.add(new ActionCard("Königlicher Erlass", "Alle anderen Spieler nehmen einen Shot.", Rarity.SUPER_RARE, null)); // TODO: Needs "target all" action
        allSuperRareCards.add(new ActionCard("Immunität", "Du kannst die nächste Straf-Karte, die du ziehst, ignorieren.", Rarity.SUPER_RARE, null)); // TODO: Needs player status effect
        allSuperRareCards.add(new ActionCard("Reset", "Dein Shot-Zähler wird auf 0 gesetzt.", Rarity.SUPER_RARE, null)); // TODO: Needs special logic
        allSuperRareCards.add(new PunishCard("Apokalypse", "Alle Spieler (auch du) nehmen einen Shot.", Rarity.SUPER_RARE, null)); // TODO: Needs "target all" action
    }
    /**
     * Stellt das Deck prozentual zusammen.
     */
    private void initializeDeck() {
        // 3. Berechne die Anzahl für jede Seltenheit
        int numSuperRare = (int) (TOTAL_DECK_SIZE * 0.15); // 15%
        int numRare = (int) (TOTAL_DECK_SIZE * 0.20);       // 20%
        int numUncommon = (int) (TOTAL_DECK_SIZE * 0.25);   // 25%
        int numCommon = TOTAL_DECK_SIZE - numSuperRare - numRare - numUncommon; // Der Rest, um auf 100% zu kommen

        // 4. Füge zufällige Karten aus den Master-Listen zum Deck hinzu
        addRandomCardsFromList(allSuperRareCards, numSuperRare);
        addRandomCardsFromList(allRareCards, numRare);
        addRandomCardsFromList(allUncommonCards, numUncommon);
        addRandomCardsFromList(allCommonCards, numCommon);
    }

    /**
     * Hilfsmethode: Wählt eine zufällige Anzahl von Karten aus einer Quell-Liste
     * und fügt sie dem Hauptdeck hinzu.
     * @param sourceList Die Liste, aus der Karten gezogen werden (z.B. allCommonCards)
     * @param count      Die Anzahl der hinzuzufügenden Karten
     */
    private void addRandomCardsFromList(List<Card> sourceList, int count) {
        if (sourceList.isEmpty()) {
            return; // Nichts zu tun, wenn keine Karten dieser Seltenheit existieren
        }
        for (int i = 0; i < count; i++) {
            // Wählt einen zufälligen Index aus der Quell-Liste
            int randomIndex = random.nextInt(sourceList.size());
            // Fügt die Karte zum Deck hinzu (erlaubt Duplikate)
            this.cards.add(sourceList.get(randomIndex));
        }
    }

    public void shuffle() {
        Collections.shuffle(cards, random);
    }

    public Card drawCard() {
        if (cards.isEmpty()) {
            System.out.println("Deck ist leer! Mische den Ablagestapel neu...");
            cards.addAll(discardPile);
            discardPile.clear();
            shuffle();
            if (cards.isEmpty()) {
                System.out.println("Keine Karten mehr zum Mischen vorhanden!");
                return null;
            }
        }
        return cards.remove(0);
    }

    public void discard(Card card) {
        discardPile.add(card);
    }
}