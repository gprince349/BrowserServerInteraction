package com.example.browsercommunication.config;

import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class SimpleWebSocketHandler extends TextWebSocketHandler {

    private Timer timer;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket connected");

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(new TextMessage("Ping from server at " + System.currentTimeMillis()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, 3000);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("Received: " + message.getPayload());
        session.sendMessage(new TextMessage("Echo: " + message.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        if (timer != null) {
            timer.cancel();
        }
        System.out.println("WebSocket closed");
    }
}
