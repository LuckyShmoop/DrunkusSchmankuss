package com.drunkus.drunkus_schmankus.game;

import com.drunkus.drunkus_schmankus.websocket.GameWebSocketHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class GameService {

    private final Map<String, GameController> activeGames = new ConcurrentHashMap<>();

    @Autowired
    @Lazy
    private GameWebSocketHandler webSocketHandler;

    // (Du kannst einen leeren Konstruktor hinzufügen oder ihn ganz weglassen)
    public GameService() {
        // Der webSocketHandler ist HIER noch null...
    }

    /**
     * Erstellt ein neues Spiel (Lobby), speichert es und gibt die Spiel-ID zurück.
     * @return Die eindeutige ID für das neue Spiel.
     */
    public String createNewGame() {
        String gameId = "game-" + UUID.randomUUID().toString().substring(0, 4);

        // HIER PASSIERT DIE MAGIE:
        // Wir erstellen einen neuen GameController und übergeben ihm seine ID
        // und den WebSocketHandler, der als seine "View" (Sender) fungiert.
        GameController newGame = new GameController(gameId, webSocketHandler);

        // Wir speichern das Spiel in unserer Liste
        activeGames.put(gameId, newGame);

        System.out.println("Neues Spiel erstellt mit ID: " + gameId);

        // Wir starten nicht mehr die run()-Schleife. Das Spiel ist jetzt
        // im Zustand WAITING_FOR_PLAYERS und wartet auf Befehle.

        return gameId;
    }

    /**
     * Findet eine GameController-Instanz anhand ihrer ID.
     * @param gameId Die ID des Spiels.
     * @return Der GameController, oder null, wenn nicht gefunden.
     */
    public GameController getGame(String gameId) {
        return activeGames.get(gameId);
    }
}