package com.ecosystem.notifications.service;



import com.ecosystem.notifications.queue_events.external_events.*;
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
public class QueueListener {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SimpMessagingTemplate notifier;

    @Autowired
    private ObservationService observer;



    private void processPersonalNotificationsStrategy(NotificationStrategy notificationStrategy, String payload){
        if (notificationStrategy==null) return;
        if (notificationStrategy.getPublicChannel()!=null){
            notificationStrategy.getPublicChannel().forEach(publicMember->{
                notifier.convertAndSend("/users/activity/public/"+publicMember, payload);

            });
        }

        if (notificationStrategy.getPrivateChannel()!=null){
            notificationStrategy.getPrivateChannel().forEach(privateMember->{
                notifier.convertAndSend("/users/activity/private/"+privateMember, payload);

            });
        }




    }


    @RabbitListener(queues = {"${users.activity_events.queue.name}"})
    public void receiveUserActivityEvent(@Payload String payload,
                                         @Header("event_type") String eventType) throws Exception {

        try {
            System.out.println(payload);
            UserEvent event = objectMapper.readValue(payload, UserEvent.class);
            notifier.convertAndSend("/users/activity/private/"+event.getContext().getUserUUID(), payload);

            processPersonalNotificationsStrategy(event.getContext().getNotificationStrategy(), payload);
        }
        catch (Exception e){
            e.printStackTrace();
            throw new AmqpRejectAndDontRequeueException("read fail");
        }




    }


    // todo тут потенциально возможны проблемы с дублированием ивента - когда ивент приходит одновременно в приват и в паблик.
    // один вариант решения - контроль corr id на ui - ивенты, имеющие один и тот же corr id, не могут быть оьбработаны больше одного раза.
    // todo добавить type  в то, что расшифровывается из payload - извлчение из header оставлю для демонстрации
    @RabbitListener(queues = {"${users.projects_events.queue.name}"})
    public void receiveProjectsEvent(@Payload String payload, @Header("event_type") String eventType) throws Exception{

        try {
            System.out.println(payload);

            ProjectEvent event = objectMapper.readValue(payload,  ProjectEvent.class);
            System.out.println(event.getType());
            ProjectEventContext context = event.getContext();
            // рассылка для канала
            notifier.convertAndSend("/projects/"+context.getProjectId(), payload);

            processPersonalNotificationsStrategy(context.getNotificationStrategy(), payload);

            /*
            // alarm strategy
            AlarmStrategy alarmStrategy = context.getAlarmStrategy();
            if (alarmStrategy!=null){
                if (alarmStrategy.getAction()==AlarmAction.SESSION_CLOSE){
                    observer.closeProjectSessionsRelatedToUsers(context.getProjectId(), alarmStrategy.getAlarmList());
                }
            }

             */
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

            processPersonalNotificationsStrategy(context.getNotificationStrategy(), payload);

        }
        catch (Exception e){
            e.printStackTrace();
            throw new AmqpRejectAndDontRequeueException("read fail");
        }


    }








}
