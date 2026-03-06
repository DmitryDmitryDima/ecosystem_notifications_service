package com.ecosystem.notifications.configuration;

import com.ecosystem.notifications.service.ObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class WebSocketEventListener {

    @Autowired
    private ObservationService observationService;



    @EventListener
    private void handleSessionSub(SessionSubscribeEvent event) {
        System.out.println("Subscribe");




        MessageHeaders headers = event.getMessage().getHeaders();



        String sessionId = SimpMessageHeaderAccessor.getSessionId(headers);


        String path = SimpMessageHeaderAccessor.getDestination(headers);

        System.out.println(path);

        observationService.addSubscription(sessionId, path);







    }




}
