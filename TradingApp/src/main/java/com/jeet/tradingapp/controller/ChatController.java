package com.jeet.tradingapp.controller;

import com.jeet.tradingapp.handler.WSHandler;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController()
public class ChatController {

    private final WSHandler wsHandler;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private static final String CHAT_TOPIC = "chit-chat";

    public ChatController(WSHandler wsHandler, KafkaTemplate<String, String> kafkaTemplate) {
        this.wsHandler = wsHandler;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/api/message")
    public Mono<Void> sendMessage(@RequestParam(required = false) String message, @RequestParam(required = false) String sessionId) {
        kafkaTemplate.send(CHAT_TOPIC, message);
        return Mono.empty();
    }

    @KafkaListener(topics = CHAT_TOPIC, groupId = "char.receiver")
    public void listen(@Payload String message) {
        // TODO:save message here
        wsHandler.triggerMessage(message, null);
    }

    @GetMapping(value = "/ws", produces = MediaType.TEXT_HTML_VALUE)
    public String websocketPage() {
        return """
                <!DOCTYPE html>
                <html>
                <head>
                    <title>WebSocket Example</title>
                </head>
                <body>
                    <h1>WebSocket Test</h1>
                    <div id="messages"></div>
                    <script>
                        const ws = new WebSocket('ws://localhost:8080/tempo');
                        ws.onmessage = function(event) {
                            const messages = document.getElementById('messages');
                            messages.innerHTML += '<p>Received: ' + event.data + '</p>';
                        };
                        ws.onopen = function(){
                            console.log("connected")
                        }
                    </script>
                </body>
                </html>
                """;
    }
}
