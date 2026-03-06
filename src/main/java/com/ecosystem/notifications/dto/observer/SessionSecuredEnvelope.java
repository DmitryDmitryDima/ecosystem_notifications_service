package com.ecosystem.notifications.dto.observer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

/*
согласно новой стратегии, извлекаем security context
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionSecuredEnvelope {

    private SecurityContext context;
    private WebSocketSession session;

}
