package com.ecosystem.notifications.configuration;

import com.ecosystem.notifications.dto.SessionInfo;
import com.ecosystem.notifications.service.ObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.UUID;

@Component
public class WebSocketEventListener {

    @Autowired
    private ObservationService observationService;



    @EventListener
    private void handleSessionSub(SessionSubscribeEvent event) {
        System.out.println("Subscribe");




        MessageHeaders headers = event.getMessage().getHeaders();

        SecurityContext securityContext = (SecurityContext) SimpMessageHeaderAccessor
                .getSessionAttributes(headers).get("securityContext");

        //System.out.println(securityContext);

        String sessionId = SimpMessageHeaderAccessor.getSessionId(headers);
        //System.out.println(sessionId);

        String path = SimpMessageHeaderAccessor.getDestination(headers);


        if (path!=null && path.startsWith("/project/")){
            UUID projectId = UUID.fromString(path.split("/project/")[1]);
            SessionInfo sessionInfo = new SessionInfo(sessionId, securityContext);
            // добавляем зависимость
            observationService.addProjectSessionInfo(projectId, sessionInfo);
        }




    }




}
