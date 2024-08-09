package com.example.ehrc.telemanas.Service;


import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SSEService {

    private final Map<String, SseEmitter> clients = new ConcurrentHashMap<>();

    public void addClient(String clientId, SseEmitter emitter) {
        clients.put(clientId, emitter);
    }

    public void removeClient(String clientId) {
        clients.remove(clientId);
    }

    public SseEmitter getClientEmitter(String clientId) {
        return clients.get(clientId);
    }

    public SseEmitter createEmitter(String clientId) {
        // Create SseEmitter with a 2-hour timeout (7200 seconds)
        SseEmitter emitter = new SseEmitter(7200_000L);
        clients.put(clientId, emitter);

        emitter.onCompletion(() -> removeClient(clientId));
        emitter.onTimeout(() -> removeClient(clientId));
        emitter.onError(e -> {
            removeClient(clientId);
            System.err.println("Error sending SSE message: " + e.getMessage());
        });

        return emitter;
    }

    public void sendCustomMessage(String clientId, String message) {
        SseEmitter emitter = clients.get(clientId);
        if (emitter != null) {
            try {
                emitter.send(message);
            } catch (IOException e) {
                emitter.completeWithError(e);
                clients.remove(clientId);
            }
        } else {
            System.out.println("Emitter not found for client: " + clientId);
        }
    }
}
