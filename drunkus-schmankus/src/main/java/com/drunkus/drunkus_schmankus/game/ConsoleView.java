package com.drunkus.drunkus_schmankus.game;

import com.drunkus.drunkus_schmankus.cards.Card;
import com.drunkus.drunkus_schmankus.player.Player;

import java.util.List;
import java.util.Scanner;

/**
 * Diese Klasse ist ausschließlich für die Darstellung des Spiels
 * und die Entgegennahme von Benutzereingaben über die Konsole zuständig.
 * Sie implementiert die IGameView-Schnittstelle.
 */
public class ConsoleView implements IGameView {

    private final Scanner scanner = new Scanner(System.in);

    // --- Methoden für die Ausgabe ---

    @Override
    public void displayWelcomeMessage() {
        System.out.println("Willkommen zu Drunkus Schmankus!");
    }

    @Override
    public void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    public void displayEmptyLines(int count) {
        for (int i = 0; i < count; i++) {
            System.out.println();
        }
    }

    @Override
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

    @Override
    public void displayPlayerStatus(List<Player> players) {
        System.out.println("\n--- Aktueller Stand ---");
        for (Player p : players) {
            System.out.println(p.getName() + " (Lvl " + p.getLevel() + ") | Shots: " + p.getShotsTakenCounter() + " | Schlucke: " + p.getSwallowsTakenCounter());
        }
    }

    @Override
    public void displayPrivateScreenHeader(Player player) {
        displayEmptyLines(4);
        System.out.println("=============================================");
        System.out.println(">>> [ANSICHT FÜR " + player.getName().toUpperCase() + "]");
    }

    @Override
    public void displayPrivateScreenFooter() {
        System.out.println("=============================================");
        displayEmptyLines(4);
    }

    // --- Methoden für die Eingabe ---

    @Override
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

    @Override
    public String askForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    @Override
    public boolean askForYesNo(String prompt) {
        System.out.println(prompt);
        String choice = askForString("").toLowerCase();
        return choice.equals("ja") || choice.equals("y");
    }

    @Override
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

    @Override
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