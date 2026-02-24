package com.ecosystem.notifications.service;



import com.ecosystem.notifications.events.*;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
//
@Service
public class RabbitMQListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SimpMessagingTemplate notifier;






    @RabbitListener(queues = {"${users.activity_events.queue.name}"})
    public void receiveUserActivityEvent(@Payload String payload,
                                         @Header("event_type") String eventType) throws Exception {

        try {
            System.out.println(payload);
            UserEvent event = objectMapper.readValue(payload, UserEvent.class);
            notifier.convertAndSend("/users/activity/private/"+event.getContext().getUserUUID(), payload);

            // дублируем в публичный канал
            if (event.getContext().isOpened()){
                notifier.convertAndSend("/users/activity/public/"+event.getContext().getUserUUID(), payload);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            throw new AmqpRejectAndDontRequeueException("read fail");
        }




    }



    @RabbitListener(queues = {"${users.projects_events.queue.name}"})
    public void receiveProjectsEvent(@Payload String payload, @Header("event_type") String eventType) throws Exception{

        try {
            System.out.println(payload);

            ProjectEvent event = objectMapper.readValue(payload,  ProjectEvent.class);
            ProjectEventContext context = event.getContext();
            // при необходимости персональной рассылки - только приватный канал.
            notifier.convertAndSend("/projects/"+context.getProjectId(), payload);

            context.getParticipants().forEach(participant->{
                notifier.convertAndSend("/users/activity/private/"+participant, payload);
            });
        }
        catch (Exception e){
            e.printStackTrace();
            throw new AmqpRejectAndDontRequeueException("read fail");
        }






    }


    @RabbitListener(queues = {"${system.projects_events.queue.name}"})
    public void receiveSystemProjectsEvent(@Payload String payload, @Header("event_type") String eventType) throws Exception{

        try {
            System.out.println(payload);
            ProjectSystemEvent event = objectMapper.readValue(payload,  ProjectSystemEvent.class);
            ProjectSystemEventContext context = event.getContext();
            // при необходимости персональной рассылки - только приватный канал.
            notifier.convertAndSend("/projects/"+context.getProjectId(), payload);

            context.getParticipants().forEach(participant->{
                notifier.convertAndSend("/users/activity/private/"+participant, payload);
            });
        }
        catch (Exception e){
            e.printStackTrace();
            throw new AmqpRejectAndDontRequeueException("read fail");
        }


    }








}
