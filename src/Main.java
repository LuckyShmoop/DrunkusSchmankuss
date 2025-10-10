import game.GameController;

public class Main {
    public static void main(String[] args) {
        System.out.println("Willkommen zu Drunkus Schmankus!");
        GameController game = new GameController();
        game.run(); // Startet die Haupt-Spiel-Schleife
    }
}