package com.drunkus.drunkus_schmankus.game;

import com.drunkus.drunkus_schmankus.player.Player;
import com.drunkus.drunkus_schmankus.websocket.GameWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;
import java.util.List;

/**
 * Eine Implementierung der IGameView, die Nachrichten über WebSockets sendet.
 * Sie kann KEINE synchronen Eingaben empfangen (askFor... Methoden).
 */
public class WebSocketGameView implements IGameView {

    private final GameWebSocketHandler webSocketHandler;
    private final List<Player> participants;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public WebSocketGameView(GameWebSocketHandler webSocketHandler, List<Player> participants) {
        this.webSocketHandler = webSocketHandler;
        this.participants = participants;
    }

    // --- Sende-Methoden ---

    @Override
    public void displayMessage(String message) {
        // Sendet eine einfache Info-Nachricht an alle Spieler im Spiel
        broadcast(createJsonMessage("GAME_UPDATE", message));
    }

    @Override
    public void displayPlayerStatus(List<Player> players) {
        // TODO: Sende ein strukturiertes Status-Update
        broadcast(createJsonMessage("STATUS_UPDATE", "Neuer Status (TODO)"));
    }

    // --- Private Sende-Methoden ---

    private void broadcast(String jsonMessage) {
        try {
            webSocketHandler.broadcastToGame(jsonMessage, participants);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToPlayer(Player player, String jsonMessage) {
        try {
            webSocketHandler.sendMessageToPlayer(player.getSessionId(), jsonMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createJsonMessage(String type, String message) {
        ObjectNode node = objectMapper.createObjectNode();
        node.put("type", type);
        node.put("message", message);
        return node.toString();
    }

    // --- NICHT UNTERSTÜTZTE "ASK"-METHODEN ---
    // Ein Webserver kann nicht anhalten und fragen. Diese werden wir später umbauen.

    @Override
    public int askForInteger(String prompt) {
        System.err.println("WARNUNG: askForInteger aufgerufen! Das ist in der Web-View nicht unterstützt.");
        return 0; // Gebe einen Standardwert zurück
    }

    @Override
    public String askForString(String prompt) {
        System.err.println("WARNUNG: askForString aufgerufen!");
        return "";
    }

    @Override
    public int askForIntegerInRange(String prompt, int min, int max) {
        // Speziell für ExenAction: Sende eine ANFRAGE an den Client
        // Wir müssen herausfinden, an wen die Anfrage geht. Das ist das Problem.
        System.err.println("WARNUNG: askForIntegerInRange aufgerufen! (z.B. für Exen). Diese Logik muss in den GameController verschoben werden.");
        // Temporärer Fix: Wir nehmen 50% an
        return 50;
    }

    @Override
    public Player askForPlayerSelection(List<Player> players, String prompt) {
        System.err.println("WARNUNG: askForPlayerSelection aufgerufen! (z.B. für Distribute).");
        // Temporärer Fix: Wir wählen den ersten Spieler auf der Liste
        return players.get(0);
    }

    // --- Unbenutzte Methoden ---
    @Override public void displayEmptyLines(int count) {}
    @Override public void displayPlayerHand(Player player) {
        // TODO: Sende ein privates Hand-Update an diesen Spieler
        // sendMessageToPlayer(player, createJsonMessage("HAND_UPDATE", player.getHand().toString()));
    }
    @Override public void displayPrivateScreenHeader(Player player) {}
    @Override public void displayPrivateScreenFooter() {}
    @Override public boolean askForYesNo(String prompt) { return true; }
    @Override public void displayWelcomeMessage() {}
}