package com.example.demo;

import com.example.demo.handler.NotificationWebSocketHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationWebSocketHandler handler;

    public NotificationController(NotificationWebSocketHandler handler) {
        this.handler = handler;
    }

    @PostMapping("/broadcast")
    public ResponseEntity<Map<String, Object>> broadcast(@RequestBody String message) {
        int sent = handler.broadcast(message);
        return ResponseEntity.ok(Map.of(
                "status", "ok",
                "sentTo", sent,
                "message", message
        ));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> stats() {
        return ResponseEntity.ok(Map.of(
                "activeConnections", handler.getActiveConnections()
        ));
    }
}
