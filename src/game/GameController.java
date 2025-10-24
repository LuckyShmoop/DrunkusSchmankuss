package game;

import cards.*;
import player.*;
import java.util.*;
import java.util.function.Function;

/**
 * Das Gehirn des Spiels. Diese Klasse enthält die gesamte Spiellogik und den Ablauf,
 * ist aber komplett von der Benutzeroberfläche (View) entkoppelt.
 */
public class GameController {
    // Der Controller kennt nur noch die Logik-Komponenten
    private List<Player> participants = new ArrayList<>();
    private Deck gameDeck;
    private int roundCounter = 0;

    // ... und die View für die Darstellung.
    private final ConsoleView view;

    private static final Map<String, Function<String, Player>> CLASS_CREATORS = Map.of(
            "1", Warrior::new, "2", Ranger::new,
            "3", Sorcerer::new, "4", Rogue::new
    );

    // Die View wird von außen übergeben (Dependency Injection)
    public GameController(ConsoleView view) {
        this.view = view;
    }

    /**
     * Die Hauptmethode, die das Spiel steuert und Wiederholungen ermöglicht.
     */
    public void run() {
        boolean playAgain;
        do {
            resetGame();
            setupGame();

            while (!isGameOver()) {
                processRound();
                if (isGameOver()) break;
            }

            announceResult();
            playAgain = view.askForYesNo("\nMöchtet ihr eine neue Runde spielen? (ja/nein)");
        } while (playAgain);

        view.displayMessage("\nDanke fürs Spielen! Bis zum nächsten Mal.");
    }

    /**
     * Setzt die Spielvariablen für eine neue Partie zurück.
     */
    private void resetGame() {
        this.participants.clear();
        this.roundCounter = 0;
    }

    /**
     * Führt das Setup für eine neue Partie durch (Spielererstellung, Starthände).
     */
    private void setupGame() {
        this.gameDeck = new Deck();
        view.displayMessage("\n--- Neues Spiel: Spieler erstellen ---");
        int amountPlayer = view.askForInteger("Wie viele Spieler seid ihr?: ");

        for (int i = 1; i <= amountPlayer; i++) {
            String name = view.askForString("\nSpieler " + i + ": Bitte Namen eingeben: ");
            view.displayMessage("Wähle deine Klasse:\n1. Warrior\n2. Ranger\n3. Sorcerer\n4. Rogue");

            Player newPlayer = null;
            while (newPlayer == null) {
                String choice = view.askForString("Deine Wahl (1-4): ");
                Function<String, Player> creator = CLASS_CREATORS.get(choice);
                if (creator != null) {
                    newPlayer = creator.apply(name);
                } else {
                    view.displayMessage("Ungültige Wahl.");
                }
            }
            participants.add(newPlayer);
            view.displayMessage(newPlayer.getName() + " ist beigetreten als " + newPlayer.getClass().getSimpleName() + "!");

            for (int j = 0; j < GameConstants.START_HAND_SIZE; j++) {
                drawStartCardFor(newPlayer);
            }
        }
    }

    private void drawStartCardFor(Player player) {
        Card drawnCard;
        do {
            drawnCard = gameDeck.drawCard();
            if (drawnCard instanceof PunishCard) {
                gameDeck.discard(drawnCard);
            }
        } while (drawnCard instanceof PunishCard);
        player.addCardToHand(drawnCard);
    }

    /**
     * Verarbeitet eine komplette Spielrunde.
     */
    public void processRound() {
        roundCounter++;
        view.displayMessage("\n=============================================");
        view.displayMessage("--- Runde " + roundCounter + " startet! ---");
        view.displayMessage("=============================================");

        List<Player> roundPlayerOrder = determinePlayerOrder();

        // Phase 1: Karten ziehen
        for (Player player : roundPlayerOrder) {
            view.displayMessage("\n" + player.getName() + " zieht eine Karte.");
            Card drawnCard = gameDeck.drawCard();
            if (drawnCard == null) return;

            view.displayMessage(">>> [Private Info für " + player.getName() + "] Du hast gezogen: " + drawnCard);

            if (drawnCard instanceof PunishCard) {
                view.displayMessage("-> Es ist eine Straf-Karte! Sie wird sofort ausgeführt.");
                drawnCard.activate(player, this.participants, this.view);
                gameDeck.discard(drawnCard);
            } else {
                player.addCardToHand(drawnCard);
            }
        }

        // Phase 2: Aktionen ausführen
        Iterator<Player> roundIterator = roundPlayerOrder.iterator();
        while (roundIterator.hasNext()) {
            Player player = roundIterator.next();
            if (!this.participants.contains(player)) continue;

            executePlayerTurn(player);

            if (isGameOver()) return;
        }

        view.displayPlayerStatus(this.participants);
    }

    private List<Player> determinePlayerOrder() {
        view.displayMessage("\n--- Reihenfolge wird ausgewürfelt ---");
        List<Player> order = new ArrayList<>(this.participants);
        Map<Player, Integer> rolls = new HashMap<>();
        Random random = new Random();
        for (Player player : order) {
            int roll = random.nextInt(GameConstants.DICE_SIDES) + 1;
            rolls.put(player, roll);
            view.displayMessage(player.getName() + " würfelt eine " + roll + ".");
        }
        Collections.shuffle(order);
        order.sort(Comparator.comparingInt(rolls::get));
        return order;
    }

    private void executePlayerTurn(Player player) {
        view.displayPrivateScreenHeader(player);
        view.displayPlayerHand(player);
        view.displayMessage("0: Keine Karte spielen");
        view.displayMessage("99: Spiel verlassen");

        int choice;
        do {
            choice = view.askForInteger("Deine Wahl: ");
        } while (!isValidChoice(choice, player.getHand().size()));

        view.displayPrivateScreenFooter();

        switch (choice) {
            case GameConstants.CHOICE_QUIT_GAME:
                view.displayMessage(player.getName() + " hat das Spiel verlassen.");
                this.participants.remove(player);
                break;
            case GameConstants.CHOICE_PASS_TURN:
                view.displayMessage(player.getName() + " spielt diese Runde keine Karte.");
                break;
            default:
                Card cardToPlay = player.getHand().get(choice - 1);
                view.displayMessage(player.getName() + " spielt: '" + cardToPlay.getTitle() + "'!");
                if (player.getHand().remove(cardToPlay)) {
                    cardToPlay.activate(player, this.participants, this.view);
                    gameDeck.discard(cardToPlay);
                }
                break;
        }
    }

    private boolean isGameOver() {
        if (participants.size() < 2) return true;
        for (Player player : participants) {
            if (player.getShotsTakenCounter() >= GameConstants.SHOTS_TO_LOSE) return true;
        }
        return false;
    }

    private void announceResult() {
        view.displayMessage("\n--- Das Spiel ist vorbei! ---");
        if (participants.size() == 1) {
            Player winner = participants.get(0);
            view.displayMessage(winner.getName() + " ist der letzte verbleibende Spieler und gewinnt!");
            view.displayPlayerStatus(List.of(winner));
        } else {
            Player loser = participants.stream()
                    .max(Comparator.comparingInt(Player::getShotsTakenCounter))
                    .orElse(null);

            if (loser != null) {
                view.displayMessage(loser.getName() + " hat mit " + loser.getShotsTakenCounter() + " Shots verloren!");
                view.displayPlayerStatus(this.participants);
            }
        }
    }

    private boolean isValidChoice(int choice, int handSize) {
        return choice == GameConstants.CHOICE_QUIT_GAME ||
                choice == GameConstants.CHOICE_PASS_TURN ||
                (choice > 0 && choice <= handSize);
    }
}