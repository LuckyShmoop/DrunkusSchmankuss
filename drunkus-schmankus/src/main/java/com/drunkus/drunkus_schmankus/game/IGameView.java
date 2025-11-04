package com.drunkus.drunkus_schmankus.game;

import com.drunkus.drunkus_schmankus.player.Player;
import java.util.List;

/**
 * Eine Schnittstelle, die alle Methoden definiert, die eine "View" (Darstellung)
 * für den GameController bereitstellen muss.
 */
public interface IGameView {

    void displayMessage(String message);
    void displayEmptyLines(int count);
    void displayPlayerHand(Player player);
    void displayPlayerStatus(List<Player> players);
    void displayPrivateScreenHeader(Player player);
    void displayPrivateScreenFooter();

    int askForInteger(String prompt);
    String askForString(String prompt);
    boolean askForYesNo(String prompt);
    Player askForPlayerSelection(List<Player> players, String prompt);
    int askForIntegerInRange(String prompt, int min, int max);

    // Diese Methoden werden für eine Web-App nicht mehr benötigt,
    // aber wir behalten sie vorerst zur Kompatibilität.
    void displayWelcomeMessage();
}