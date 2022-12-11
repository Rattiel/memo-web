package com.jongil.memo.global.config.websocket;

import com.jongil.memo.global.config.security.Ownable;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecuredStompSender {
    private final SimpMessagingTemplate websocket;

    public void send(String destination, Ownable data) {
        this.websocket.convertAndSendToUser(data.getPrincipalName(), destination, data);
    }
}

