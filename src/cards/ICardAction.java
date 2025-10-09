package cards;

import player.Player; // Importiert die Basisklasse aus dem Player-Package

public interface ICardAction {

    // Führt die spezifische Karten-Logik auf dem Zielspieler aus
    void execute(Player target);

    // Optional: Könnte auch das gesamte Spiel beeinflussen (z.B. indem es den Controller erhält)
    // void execute(Player target, game.GameController controller); 
}