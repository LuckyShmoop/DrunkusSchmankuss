package com.drunkus.drunkus_schmankus.websocket;

import com.drunkus.drunkus_schmankus.game.GameController;
import com.drunkus.drunkus_schmankus.game.GameService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // NEU: Wir brauchen den GameService, um Spiele zu finden und Aktionen weiterzuleiten
    private final GameService gameService;
    // NEU: Ein Werkzeug, um Text (JSON) in Java-Objekte umzuwandeln
    private final ObjectMapper objectMapper = new ObjectMapper();

    public GameWebSocketHandler(GameService gameService) {
        this.gameService = gameService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.put(session.getId(), session);
        System.out.println("Neue WebSocket-Verbindung hergestellt: " + session.getId());

        // Sende eine Willkommensnachricht
        session.sendMessage(new TextMessage("{\"type\": \"CONNECTION_SUCCESS\", \"sessionId\": \"" + session.getId() + "\"}"));
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Nachricht empfangen von " + session.getId() + ": " + payload);

        try {
            // Wir parsen die Nachricht als JSON
            JsonNode jsonNode = objectMapper.readTree(payload);
            String messageType = jsonNode.get("type").asText();

            // Finde das Spiel, auf das sich die Nachricht bezieht
            String gameId = jsonNode.get("gameId").asText();
            GameController game = gameService.getGame(gameId);

            if (game == null) {
                sendMessageToPlayer(session.getId(), "{\"type\": \"ERROR\", \"message\": \"Spiel nicht gefunden.\"}");
                return;
            }

            // Leite die Aktion an den richtigen GameController weiter
            switch (messageType) {
                case "JOIN_GAME":
                    String playerName = jsonNode.get("name").asText();
                    String classChoice = jsonNode.get("classChoice").asText();
                    game.addPlayer(session.getId(), playerName, classChoice);
                    break;
                case "START_GAME":
                    game.startGame(session.getId());
                    break;
                case "PLAYER_ACTION":
                    // z.B. jsonNode.get("action").asText() könnte "PLAY_CARD:3" sein
                    String action = jsonNode.get("action").asText();
                    game.handlePlayerAction(session.getId(), action);
                    break;
                // TODO: Weitere Aktionen wie "QUIT" usw.
            }

        } catch (Exception e) {
            e.printStackTrace();
            sendMessageToPlayer(session.getId(), "{\"type\": \"ERROR\", \"message\": \"Ungültige Nachricht.\"}");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());
        System.out.println("Verbindung geschlossen: " + session.getId());
        // TODO: Teile dem GameController mit, dass der Spieler das Spiel verlassen hat
    }

    // --- Unsere Sende-Methoden ---

    public void sendMessageToPlayer(String sessionId, String message) throws IOException {
        WebSocketSession session = sessions.get(sessionId);
        if (session != null && session.isOpen()) {
            session.sendMessage(new TextMessage(message));
        }
    }

    public void broadcast(String message) throws IOException {
        for (WebSocketSession session : sessions.values()) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }

    // NEU: Eine schlauere Broadcast-Methode für den GameController
    public void broadcastToGame(String message, java.util.List<com.drunkus.drunkus_schmankus.player.Player> players) throws IOException {
        for (com.drunkus.drunkus_schmankus.player.Player player : players) {
            sendMessageToPlayer(player.getSessionId(), message);
        }
    }
}