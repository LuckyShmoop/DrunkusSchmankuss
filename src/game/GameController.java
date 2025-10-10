package game;

import cards.Card;
import cards.Deck;
import cards.PunishCard;
import player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

public class GameController {
    private List<Player> participants = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private int roundCounter = 0;
    private Deck gameDeck;

    private static final Map<String, Function<String, Player>> CLASS_CREATORS;

    static {
        CLASS_CREATORS = Map.of(
                "1", Warrior::new, "2", Ranger::new,
                "3", Sorcerer::new, "4", Rogue::new
        );
    }

    /**
     * Die Hauptmethode, die das Spiel steuert und Wiederholungen ermöglicht.
     */
    public void run() {
        boolean playAgain;
        do {
            // Spielzustand für eine neue Partie zurücksetzen
            resetGame();
            // Neues Spiel einrichten
            setupGame();

            // Die Spiel-Schleife für die aktuelle Partie
            while (!isGameOver()) {
                processRound();
                // Breche ab, wenn die Runde das Spiel beendet hat (z.B. durch zu wenige Spieler)
                if (isGameOver()) break;
            }

            // Gewinner/Verlierer bekannt geben
            announceWinner();

            // Fragen, ob eine neue Partie gestartet werden soll
            System.out.println("\nMöchtet ihr eine neue Runde spielen? (ja/nein)");
            String choice = scanner.nextLine().toLowerCase();
            playAgain = choice.equals("ja") || choice.equals("y");

        } while (playAgain);

        System.out.println("\nDanke fürs Spielen! Bis zum nächsten Mal.");
    }

    /**
     * Setzt die Spielvariablen für eine neue Partie zurück.
     */
    private void resetGame() {
        this.participants.clear();
        this.roundCounter = 0;
    }

    /**
     * Fragt die Spieleranzahl, Namen und Klassen ab und gibt jedem Spieler 3 Startkarten.
     */
    public void setupGame() {
        this.gameDeck = new Deck(); // Für jede neue Partie ein frisches Deck
        System.out.println("\n--- Neues Spiel: Spieler erstellen ---");
        System.out.println("--- Wie viele Spieler seid ihr? ---");
        int amountPlayer = 0;
        while (amountPlayer <= 0) {
            try {
                String input = scanner.nextLine();
                if (input.trim().isEmpty()) continue;
                amountPlayer = Integer.parseInt(input);
                if (amountPlayer <= 0) System.out.println("Bitte eine positive Zahl eingeben.");
            } catch (NumberFormatException e) { System.out.println("Ungültige Eingabe. Bitte eine Zahl eingeben."); }
        }
        for (int i = 1; i <= amountPlayer; i++) {
            System.out.println("\nSpieler " + i + ": Bitte Namen eingeben:");
            String name = scanner.nextLine();
            displayClassOptions();
            Player newPlayer = null;
            while (newPlayer == null) {
                System.out.print("Deine Wahl (1-4): ");
                String choice = scanner.nextLine();
                Function<String, Player> creator = CLASS_CREATORS.get(choice);
                if (creator != null) {
                    newPlayer = creator.apply(name);
                } else { System.out.println("Ungültige Wahl. Bitte wähle eine Zahl zwischen 1 und 4."); }
            }
            participants.add(newPlayer);
            System.out.println(newPlayer.getName() + " ist beigetreten als " + newPlayer.getClass().getSimpleName() + "!");
            System.out.println(newPlayer.getName() + " zieht 3 Startkarten...");
            for (int j = 0; j < 3; j++) {
                Card drawnCard;
                do {
                    drawnCard = gameDeck.drawCard();
                    if (drawnCard instanceof PunishCard) gameDeck.discard(drawnCard);
                } while (drawnCard instanceof PunishCard);
                newPlayer.addCardToHand(drawnCard);
            }
            System.out.println(newPlayer.getName() + " hat seine Starthand erhalten.");
        }
        System.out.println("\nAlle " + participants.size() + " Spieler sind erstellt und bereit!");
    }

    /**
     * Verarbeitet eine komplette Spielrunde.
     */
    public void processRound() {
        roundCounter++;
        System.out.println("\n=============================================");
        System.out.println("--- Runde " + roundCounter + " startet! ---");
        System.out.println("=============================================");

        // Würfelphase
        System.out.println("\n--- Reihenfolge wird ausgewürfelt ---");
        List<Player> roundPlayerOrder = new ArrayList<>(this.participants);
        java.util.Map<Player, Integer> playerRolls = new java.util.HashMap<>();
        java.util.Random random = new java.util.Random();
        for (Player player : roundPlayerOrder) {
            int roll = random.nextInt(100) + 1;
            playerRolls.put(player, roll);
            System.out.println(player.getName() + " würfelt eine " + roll + ".");
        }
        java.util.Collections.shuffle(roundPlayerOrder);
        roundPlayerOrder.sort(java.util.Comparator.comparingInt(playerRolls::get));
        System.out.println("\nDie Spielerreihenfolge für diese Runde ist:");
        for (int i = 0; i < roundPlayerOrder.size(); i++) {
            System.out.println((i + 1) + ". " + roundPlayerOrder.get(i).getName());
        }

        // Phase 1: Karten ziehen
        System.out.println("\n--- Phase 1: Karten ziehen ---");
        for (Player player : roundPlayerOrder) {
            Card drawnCard = gameDeck.drawCard();
            if (drawnCard == null) return;
            System.out.println("\n" + player.getName() + " zieht: " + drawnCard);
            if (drawnCard instanceof PunishCard) {
                System.out.println("-> Eine Straf-Karte! Sie wird sofort ausgeführt.");
                drawnCard.activate(player, this.participants, this.scanner);
                gameDeck.discard(drawnCard);
            } else {
                System.out.println("-> Diese Karte kommt auf deine Hand.");
                player.addCardToHand(drawnCard);
            }
        }

        // Phase 2: Aktionen ausführen
        System.out.println("\n--- Phase 2: Aktionen ausführen ---");
        java.util.Iterator<Player> roundIterator = roundPlayerOrder.iterator();
        while (roundIterator.hasNext()) {
            Player player = roundIterator.next();
            // Wenn der Spieler in dieser Runde bereits entfernt wurde (selten, aber sicher ist sicher), überspringen.
            if (!this.participants.contains(player)) continue;

            System.out.println("\n" + player.getName() + " ist am Zug.");
            List<Card> hand = player.getHand();
            if (hand.isEmpty()) System.out.println(player.getName() + " hat keine Karten auf der Hand.");
            else {
                System.out.println("Deine Hand:");
                for (int i = 0; i < hand.size(); i++) System.out.println((i + 1) + ": " + hand.get(i));
            }
            System.out.println("0: Keine Karte spielen");
            System.out.println("99: Spiel verlassen");
            int choice = -1;
            while (!isValidChoice(choice, hand.size())) {
                System.out.print("Welche Aktion möchtest du ausführen?: ");
                try {
                    String input = scanner.nextLine();
                    choice = Integer.parseInt(input);
                    if (!isValidChoice(choice, hand.size())) System.out.println("Ungültige Eingabe.");
                } catch (NumberFormatException e) { System.out.println("Bitte gib eine gültige Zahl ein."); }
            }
            if (choice == 99) {
                System.out.println(player.getName() + " hat das Spiel verlassen.");
                roundIterator.remove();
                this.participants.remove(player);
                if (isGameOver()) return; // Spiel sofort beenden
                continue;
            } else if (choice > 0) {
                Card cardToPlay = hand.get(choice - 1);
                System.out.println(player.getName() + " spielt: " + cardToPlay.getTitle());
                if (hand.remove(cardToPlay)) {
                    cardToPlay.activate(player, this.participants, this.scanner);
                    gameDeck.discard(cardToPlay);
                }
            } else {
                System.out.println(player.getName() + " spielt diese Runde keine Karte.");
            }
        }

        if (isGameOver()) return;

        System.out.println("\n--- Rundenende: Aktueller Stand ---");
        for (Player p : this.participants) {
            System.out.println(p.getName() + " | Shots: " + p.getShotsTakenCounter() + " | Schlucke: " + p.getSwallowsTakenCounter());
        }
    }

    private boolean isGameOver() {
        if (participants.size() < 2) return true;
        for (Player player : participants) {
            if (player.getShotsTakenCounter() >= 10) return true;
        }
        return false;
    }

    private void announceWinner() {
        System.out.println("\n--- Das Spiel ist vorbei! ---");
        if (participants.size() == 1) {
            Player winner = participants.get(0);
            System.out.println(winner.getName() + " ist der letzte verbleibende Spieler und gewinnt das Spiel!");
            System.out.println("\n--- Endstatistik für " + winner.getName() + " ---");
            System.out.println("Getrunkene Shots: " + winner.getShotsTakenCounter());
            System.out.println("Getrunkene Schlucke: " + winner.getSwallowsTakenCounter());
        } else {
            Player loser = null;
            int maxShots = -1;
            for (Player player : participants) {
                if (player.getShotsTakenCounter() > maxShots) {
                    maxShots = player.getShotsTakenCounter();
                    loser = player;
                }
            }
            if (loser != null) {
                System.out.println(loser.getName() + " hat mit " + loser.getShotsTakenCounter() + " Shots verloren!");
                System.out.println("\n--- Endstatistik ---");
                for(Player player : participants){
                    System.out.println(player.getName() + " | Shots: " + player.getShotsTakenCounter() + " | Schlucke: " + player.getSwallowsTakenCounter());
                }
            }
        }
    }

    private void displayClassOptions() {
        System.out.println("Wähle deine Klasse:");
        System.out.println("1. Warrior");
        System.out.println("2. Ranger");
        System.out.println("3. Sorcerer");
        System.out.println("4. Rogue");
    }

    private boolean isValidChoice(int choice, int handSize) {
        if (choice == 99 || choice == 0) return true;
        return choice > 0 && choice <= handSize;
    }
}