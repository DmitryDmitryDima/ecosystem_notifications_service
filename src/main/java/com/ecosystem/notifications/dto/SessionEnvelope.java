package com.ecosystem.notifications.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;


@Data
@NoArgsConstructor
public class SessionEnvelope {
    private WebSocketSession session;

    private SessionEnvelopePayload payload;

    public SessionEnvelope(WebSocketSession session){
        this.session = session;
    }
}
