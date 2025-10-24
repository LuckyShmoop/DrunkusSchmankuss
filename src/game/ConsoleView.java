package game;

import cards.Card;
import player.Player;

import java.util.List;
import java.util.Scanner;

/**
 * Diese Klasse ist ausschließlich für die Darstellung des Spiels
 * und die Entgegennahme von Benutzereingaben über die Konsole zuständig.
 */
public class ConsoleView {

    private final Scanner scanner = new Scanner(System.in);

    // --- Methoden für die Ausgabe ---

    public void displayWelcomeMessage() {
        System.out.println("Willkommen zu Drunkus Schmankus!");
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayEmptyLines(int count) {
        for (int i = 0; i < count; i++) {
            System.out.println();
        }
    }

    public void displayPlayerHand(Player player) {
        System.out.println("Deine Hand:");
        List<Card> hand = player.getHand();
        if (hand.isEmpty()) {
            System.out.println("Du hast keine Karten auf der Hand.");
        } else {
            for (int i = 0; i < hand.size(); i++) {
                System.out.println((i + 1) + ": " + hand.get(i));
            }
        }
    }

    public void displayPlayerStatus(List<Player> players) {
        System.out.println("\n--- Aktueller Stand ---");
        for (Player p : players) {
            System.out.println(p.getName() + " | Shots: " + p.getShotsTakenCounter() + " | Schlucke: " + p.getSwallowsTakenCounter());
        }
    }

    public void displayPrivateScreenHeader(Player player) {
        displayEmptyLines(4);
        System.out.println("=============================================");
        System.out.println(">>> [ANSICHT FÜR " + player.getName().toUpperCase() + "]");
    }

    public void displayPrivateScreenFooter() {
        System.out.println("=============================================");
        displayEmptyLines(4);
    }

    // --- Methoden für die Eingabe ---

    public int askForInteger(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                String input = scanner.nextLine();
                if (!input.trim().isEmpty()) {
                    return Integer.parseInt(input);
                }
            } catch (NumberFormatException e) {
                System.out.print("Ungültige Eingabe. Bitte eine Zahl eingeben: ");
            }
        }
    }

    public String askForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public boolean askForYesNo(String prompt) {
        System.out.println(prompt);
        String choice = askForString("").toLowerCase();
        return choice.equals("ja") || choice.equals("y");
    }

    /**
     * Lässt den Benutzer einen Spieler aus einer Liste auswählen.
     * @param players Die Liste der möglichen Ziele.
     * @param prompt Die Frage, die dem Benutzer gestellt wird.
     * @return Der ausgewählte Spieler.
     */
    public Player askForPlayerSelection(List<Player> players, String prompt) {
        displayMessage(prompt);
        for (int i = 0; i < players.size(); i++) {
            displayMessage((i + 1) + ": " + players.get(i).getName());
        }
        int choice;
        do {
            choice = askForInteger("Deine Wahl (1-" + players.size() + "): ");
        } while (choice < 1 || choice > players.size());
        return players.get(choice - 1);
    }

    /**
     * Fragt nach einer Zahl innerhalb eines bestimmten Bereichs.
     * @param prompt Die Frage an den Benutzer.
     * @param min Der erlaubte Mindestwert.
     * @param max Der erlaubte Höchstwert.
     * @return Die gültige Zahl.
     */
    public int askForIntegerInRange(String prompt, int min, int max) {
        int value;
        do {
            value = askForInteger(prompt);
            if (value < min || value > max) {
                displayMessage("Bitte eine Zahl zwischen " + min + " und " + max + " eingeben.");
            }
        } while (value < min || value > max);
        return value;
    }
}