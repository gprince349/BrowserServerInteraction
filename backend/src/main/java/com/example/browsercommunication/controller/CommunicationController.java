package com.example.browsercommunication.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/api")
public class CommunicationController {

    @GetMapping("/hello")
    public String hello() {
        return "{\"message\": \"Hello from Spring Boot HTTP!\"}";
    }

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter sse() {
        SseEmitter emitter = new SseEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();

        executor.execute(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    emitter.send("Hello via SSE at " + System.currentTimeMillis());
                    Thread.sleep(2000);
                }
                emitter.complete();
            } catch (IOException | InterruptedException ex) {
                emitter.completeWithError(ex);
            }
        });

        executor.shutdown();
        return emitter;
    }

    @GetMapping("/long-poll")
    public String longPoll() throws InterruptedException {
        Thread.sleep(5000); // Simulate delay
        return "{\"message\": \"Long Polling response after delay!\"}";
    }
}
