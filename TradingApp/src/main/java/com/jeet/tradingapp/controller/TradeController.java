package com.jeet.tradingapp.controller;

import com.jeet.tradingapp.handler.WSHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Configuration
@RestController()
public class TradeController {

    @Autowired
    private WSHandler wsHandler;

    @PostMapping("/api/trigger")
    public Mono<String> triggerWebSocketMessage(@RequestParam(required = false) String message, @RequestParam(required = false) String sessionId) {
        return wsHandler.triggerMessage(message, sessionId);
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
