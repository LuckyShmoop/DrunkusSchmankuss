package com.drunkus.drunkus_schmankus.lobby;

import com.drunkus.drunkus_schmankus.game.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/game") // Alle URLs in dieser Klasse beginnen mit /api/game
public class GameLobbyController {

    // Spring Boot wird unseren GameService hier automatisch "injizieren"
    private final GameService gameService;

    @Autowired
    public GameLobbyController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Erstellt ein neues Spiel, wenn die URL /api/game/create aufgerufen wird.
     * @return Eine JSON-Antwort, die die neue Spiel-ID enthält.
     */
    @GetMapping("/create")
    public ResponseEntity<?> createNewGame() {
        // Rufe unseren Lobby-Manager auf
        String gameId = gameService.createNewGame();

        // Erstelle eine einfache Antwort für den Client (z.B. Next.js)
        // Das wird zu: { "gameId": "game-xxxx" }
        Map<String, String> response = Map.of("gameId", gameId);

        return ResponseEntity.ok(response);
    }
}