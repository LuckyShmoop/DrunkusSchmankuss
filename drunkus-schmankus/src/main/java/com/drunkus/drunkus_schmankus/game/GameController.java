package com.drunkus.drunkus_schmankus.game;

import com.drunkus.drunkus_schmankus.cards.*;
import com.drunkus.drunkus_schmankus.player.*;
import com.drunkus.drunkus_schmankus.websocket.GameWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction; // KORRIGIERT: (war Function)

public class GameController {

    // WIR SPEICHERN DEN AKTUELLEN ZUSTAND DES SPIELS
    private enum GameState {
        WAITING_FOR_PLAYERS,
        DRAWING_CARDS,
        WAITING_FOR_ACTION,
        GAME_OVER
    }

    private List<Player> participants = new ArrayList<>();
    private Deck gameDeck;
    private int roundCounter = 0;
    private GameState currentState;
    private Player activePlayer;

    private final String gameId;
    private final GameWebSocketHandler webSocketHandler;

    // KORRIGIERT: Verwendet jetzt BiFunction (nimmt 2 Strings, gibt Player zurück)
    private static final Map<String, BiFunction<String, String, Player>> CLASS_CREATORS = Map.of(
            "1", Warrior::new,
            "2", Ranger::new,
            "3", Sorcerer::new,
            "4", Rogue::new
    );

    public GameController(String gameId, GameWebSocketHandler webSocketHandler) {
        this.gameId = gameId;
        this.webSocketHandler = webSocketHandler;
        this.currentState = GameState.WAITING_FOR_PLAYERS;
        this.gameDeck = new Deck();

        broadcast("Spiel " + gameId + " erstellt. Warten auf Spieler...");
    }

    // --- ÖFFENTLICHE METHODEN (von außen aufgerufen) ---

    public void addPlayer(String sessionId, String name, String classChoice) {
        if (currentState != GameState.WAITING_FOR_PLAYERS) {
            sendMessageToPlayer(sessionId, "Fehler: Spiel läuft bereits.");
            return;
        }

        // KORRIGIERT: Verwendet jetzt BiFunction
        BiFunction<String, String, Player> creator = CLASS_CREATORS.get(classChoice);
        if (creator != null) {
            // KORRIGIERT: creator.apply ruft jetzt (name, sessionId) auf
            Player newPlayer = creator.apply(name, sessionId);
            participants.add(newPlayer);

            // Startkarten ziehen
            for (int j = 0; j < GameConstants.START_HAND_SIZE; j++) {
                drawStartCardFor(newPlayer);
            }

            broadcast(name + " ist dem Spiel beigetreten als " + newPlayer.getClass().getSimpleName());
            // TODO: Sende dem Spieler seine private Hand
            // sendMessageToPlayer(sessionId, "{\"type\": \"HAND_UPDATE\", \"hand\": " + newPlayer.getHand().toString() + "}");

        } else {
            sendMessageToPlayer(sessionId, "Fehler: Ungültige Klasse.");
        }
    }

    public void startGame(String starterSessionId) {
        // TODO: Prüfen, ob der starterSessionId der Ersteller der Lobby ist
        if (currentState != GameState.WAITING_FOR_PLAYERS || participants.size() < 1) { // Testen mit 1 Spieler
            sendMessageToPlayer(starterSessionId, "Fehler: Nicht genügend Spieler.");
            return;
        }

        broadcast("Das Spiel beginnt!");
        startNewRound();
    }

    public void handlePlayerAction(String sessionId, String action) {
        // TODO: JSON-Aktion parsen (z.B. "PLAY_CARD:3" oder "QUIT")

        Player player = participants.stream()
                .filter(p -> p.getSessionId().equals(sessionId))
                .findFirst()
                .orElse(null);

        if (player == null || player != activePlayer) {
            sendMessageToPlayer(sessionId, "Fehler: Du bist nicht am Zug.");
            return;
        }

        // ... Logik für die Aktion ...
        // z.B. if (action.equals("QUIT")) { ... }

        // Nach der Aktion: Nächsten Spieler bestimmen oder Runde beenden
        // ...

        // Nächsten Spieler an die Reihe kommen lassen
        // nextPlayerTurn();
    }

    // --- PRIVATE SPIEL-LOGIK (intern) ---

    private void startNewRound() {
        roundCounter++;
        broadcast("======================================");
        broadcast("Runde " + roundCounter + " startet!");

        // TODO: Würfellogik implementieren (determinePlayerOrder)

        // TODO: Karten-Zieh-Phase (executeDrawingPhase)
        // ...

        // Starte den ersten Zug
        activePlayer = participants.get(0);
        currentState = GameState.WAITING_FOR_ACTION;
        broadcast("Es ist " + activePlayer.getName() + " am Zug.");
        // TODO: Sende privates Update an den aktiven Spieler
        // sendMessageToPlayer(activePlayer.getSessionId(), "{\"type\": \"YOUR_TURN\", \"hand\": " + activePlayer.getHand() + "}");
    }

    // KORRIGIERT: Code wieder eingefügt
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

    // KORRIGIERT: Code wieder eingefügt
    private boolean isGameOver() {
        if (participants.size() < 2) return true;
        for (Player player : participants) {
            if (player.getShotsTakenCounter() >= GameConstants.SHOTS_TO_LOSE) return true;
        }
        return false;
    }


    // --- NEUE SENDE-METHODEN (ehemals View) ---

    /**
     * Sendet eine Nachricht an ALLE Spieler in diesem Spiel.
     */
    private void broadcast(String message) {
        try {
            // Wir senden die Nachricht und die Liste unserer Teilnehmer
            webSocketHandler.broadcastToGame(message, this.participants);
        } catch (IOException e) {
            e.printStackTrace();
        }
    } // KORRIGIERT: Fehlende Klammer war hier

    /**
     * Sendet eine private Nachricht an einen bestimmten Spieler.
     */
    private void sendMessageToPlayer(String sessionId, String message) {
        try {
            webSocketHandler.sendMessageToPlayer(sessionId, message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}