import game.ConsoleView;
import game.GameController;

public class Main {
    public static void main(String[] args) {
        // 1. Erstelle die View
        ConsoleView view = new ConsoleView();

        // 2. Erstelle den Controller und gib ihm die View
        GameController game = new GameController(view);

        // 3. Starte das Spiel
        view.displayWelcomeMessage();
        game.run();
    }
}