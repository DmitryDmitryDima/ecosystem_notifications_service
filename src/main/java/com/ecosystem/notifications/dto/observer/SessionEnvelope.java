package com.ecosystem.notifications.dto.observer;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
public class SessionEnvelope {
    private WebSocketSession session;

    private List<SessionEnvelopePayload> payload = new ArrayList<>();

    public SessionEnvelope(WebSocketSession session){
        this.session = session;
    }
}
