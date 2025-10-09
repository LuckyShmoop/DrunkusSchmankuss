package game;

import cards.Card;
import cards.Deck;
import player.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;


public class GameController{
    private  List<Player> participants = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
    private int roundCounter = 0;

    private static final Map<String, Function<String, Player>> CLASS_CREATORS;

    static {
        // Statischer Block, um die Map einmalig beim Programmstart zu befüllen
        CLASS_CREATORS = Map.of(
                "1", Warrior::new,    // Nimmt String (Name) entgegen und ruft new Warrior(Name) auf
                "2", Ranger::new,     // Ruft new Ranger(Name) auf
                "3", Sorcerer::new,
                "4", Rogue::new
        );
    }
    // Hilfsmethode zur Anzeige
    private void displayClassOptions() {
        System.out.println("Hallo! Wähle deine Klasse:");
        System.out.println("1. Warrior");
        System.out.println("2. Ranger");
        System.out.println("3. Sorcerer");
        System.out.println("4. Rogue");
    }
    public void setupGame() {
        System.out.println("--- Spielstart: Spieler erstellen ---");
        System.out.println("--- Wie viele Spieler seid ihr? ---");

        int amountPlayer = scanner.nextInt();
        scanner.nextLine(); // Puffer leeren

        for (int i = 1; i <= amountPlayer; i++) {
            System.out.println("\nSpieler " + i + ": Bitte Namen eingeben:");
            String name = scanner.nextLine();

            displayClassOptions();

            Player newPlayer = null;
            while (newPlayer == null) {
                System.out.print("Deine Wahl (1-4): ");
                String choice = scanner.nextLine();

                // Holt die Erstellungs-Funktion für die gewählte Nummer
                Function<String, Player> creator = CLASS_CREATORS.get(choice);
                if (creator != null) {
                    newPlayer = creator.apply(name);
                } else {
                    System.out.println("Ungültige Wahl. Bitte wähle eine Zahl zwischen 1 und 4.");
                }
            }

            participants.add(newPlayer);
            System.out.println(newPlayer.getName() + " ist beigetreten als " + newPlayer.getClass().getSimpleName() + "!");
        }

        System.out.println("\nAlle " + participants.size() + " Spieler sind erstellt und bereit!");
    }
    public void processRound() {
        roundCounter++;
        System.out.println("--- Das ist Runde " + roundCounter + "!");

        // Liste, um die NICHT sofort gespielten Karten zu speichern
        // Du brauchst eine Struktur, die speichert, wer welche Karten besitzt!
        // Für dieses Beispiel lassen wir den Spieler die Karte erstmal selbst speichern.

        // ==========================================================
        // PHASE 1: KARTEN ZIEHEN & PFLICHT-EFFEKTE AUSFÜHREN
        // ==========================================================
        System.out.println("\n--- Phase 1: Karten ziehen & Pflicht-Effekte ---");
        for (Player player : participants) {

            Card drawnCard = gameDeck.drawCard();
            System.out.print("\n" + player.getName() + " zieht: " + drawnCard.getTitle() + "...");

            // Die Regel-Implementierung!
            if (drawnCard instanceof PunishCard) {
                // Regel: MUSS sofort gespielt werden
                System.out.println(" (Ist eine Straf-Karte und wird sofort ausgelöst!)");
                drawnCard.activate(player);

            } else {
                // Regel: Darf behalten werden
                System.out.println(" (Darf behalten werden.)");

                // HINWEIS: Hier brauchst du die Logik, um die Karte beim Spieler zu speichern!
                // Da Player diese Logik noch nicht hat, ist das ein notwendiger nächster Schritt.
                // Beispiel-Aufruf (Musst du in Player implementieren):
                player.addCardToHand(drawnCard);
            }
        }

        // ... PHASE 2: SPIELERAKTIONEN AUSFÜHREN ...
        // In Phase 2 musst du dann die Option anbieten, EINE der behaltenen Karten auszuspielen.
    }
}


