package com.example.ehrc.telemanas.Controller;

import com.example.ehrc.telemanas.Service.SSEService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@RestController
@RequestMapping("/api")
public class SSEController {

    private final SSEService sseService;
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(10);

    public SSEController(SSEService sseService) {
        this.sseService = sseService;
    }

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamSse(@RequestParam String clientId) {
        return sseService.createEmitter(clientId);
    }

    @PostMapping("/send")
    public void sendCustomMessage(@RequestParam String clientId, @RequestParam String message) {
        sseService.sendCustomMessage(clientId, message);
    }
}
