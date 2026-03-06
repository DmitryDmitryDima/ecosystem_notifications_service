package com.ecosystem.notifications.service;


import com.ecosystem.notifications.dto.observer.*;
import com.ecosystem.notifications.dto.api.UsernameUUIDPair;
import com.ecosystem.notifications.queue_events.external_events.ProjectEventContext;
import com.ecosystem.notifications.queue_events.observer_events.ObserverEventType;
import com.ecosystem.notifications.queue_events.observer_events.ProjectObserverEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ObservationService {


    @Autowired
    private ApplicationEventPublisher publisher;

    private final ConcurrentHashMap<String, SessionSecuredEnvelope> sessionStore = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, List<Subscription>> userToSubAssosiation = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<UUID, List<Subscription>> projectToSubAssosiation = new ConcurrentHashMap<>();

    // связываем сессию и security context
    public void registerSecuredSession(WebSocketSession session, SecurityContext context){
        SessionSecuredEnvelope sessionSecuredEnvelope = new SessionSecuredEnvelope(context, session);
        sessionStore.put(session.getId(), sessionSecuredEnvelope);
    }

    private void injectSub(Subscription subsription, String sessionId){
        if (subsription==null) return;
        // гарантируем, что сессия находится в главном хранилище
        AtomicReference<SessionSecuredEnvelope> touchedEnvelope = new AtomicReference<>();

        sessionStore.computeIfPresent(sessionId, (id, envelope)->{

            touchedEnvelope.set(envelope);

            subsription.setUsername(envelope.getContext().getUsername());
            subsription.setUserUUID(envelope.getContext().getUuid());

            UUID userUUID = envelope.getContext().getUuid();


            userToSubAssosiation.compute(userUUID, (uuid,subs)->{
                if (subs==null){
                    subs = new ArrayList<>();
                }
                subs.add(subsription);
                return subs;
            });



            return envelope;
        });

        // операция не была совершена!
        if (touchedEnvelope.get()==null) return;

        if (subsription.getSessionPayload() instanceof SessionProjectPayload sessionProjectPayload){
            projectToSubAssosiation.compute(sessionProjectPayload.getProjectId(), (projectId, subs)->{
                if (subs == null){
                    subs = new ArrayList<>();
                }
                subs.add(subsription);
                return subs;
            });

            ProjectObserverEvent projectObserverEvent = new ProjectObserverEvent();
            projectObserverEvent.setType(ObserverEventType.PROJECT_SUB.getValue());
            ProjectEventContext context = new ProjectEventContext();
            context.setUsername(subsription.getUsername());
            context.setUserUUID(subsription.getUserUUID());
            context.setProjectId(sessionProjectPayload.getProjectId());
            projectObserverEvent.setContext(context);
            publisher.publishEvent(projectObserverEvent);
        }
    }

    public void addSubscription(String sessionId, String path){
        if (path == null) return;
        Subscription subscription = null;
        if (path.startsWith("/projects/")){
            UUID projectId = UUID.fromString(path.split("/projects/")[1]);
            SessionProjectPayload projectPayload = new SessionProjectPayload(projectId);
            subscription = new Subscription(sessionId, projectPayload);


        }

        if (path.startsWith("/users/activity/private/")){
            UUID targetUUID = UUID.fromString(path.split("/users/activity/private/")[1]);
            SessionPersonalSubPayload personalSubPayload = new SessionPersonalSubPayload(false, targetUUID);
            subscription = new Subscription(sessionId, personalSubPayload);
        }

        if (path.startsWith("/users/activity/public/")){
            UUID targetUUID = UUID.fromString(path.split("/users/activity/public/")[1]);
            SessionPersonalSubPayload personalSubPayload = new SessionPersonalSubPayload(true, targetUUID);
            subscription = new Subscription(sessionId, personalSubPayload);
        }



        injectSub(subscription, sessionId);
    }

    public void unregisterSecuredSession(String sessionId){
        SessionSecuredEnvelope removed = sessionStore.remove(sessionId);
        if (removed == null) return;
        UUID userUUID = removed.getContext().getUuid();
        List<Subscription> assosiatedSubs = new ArrayList<>();
        userToSubAssosiation.compute(userUUID, (uuid, subs)->{
            if (subs==null) return null;
            subs.removeIf(sub->{

                boolean isRelated = sub.getSessionId().equals(sessionId);
                if (isRelated ){
                    assosiatedSubs.add(sub);
                }
                return isRelated;

            });

            if (subs.isEmpty()){subs = null;}
            return subs;
        });

        assosiatedSubs.forEach(subscription -> {
            if (subscription.getSessionPayload() instanceof SessionProjectPayload sessionProjectPayload){
                projectToSubAssosiation.computeIfPresent(sessionProjectPayload.getProjectId(), (projectUUID, subs)->{
                    subs.removeIf(sub->sub.getSessionId().equals(sessionId));
                    if (subs.isEmpty()){
                        subs = null;
                    }
                    return subs;
                });

                ProjectObserverEvent projectObserverEvent = new ProjectObserverEvent();
                projectObserverEvent.setType(ObserverEventType.PROJECT_UNSUB.getValue());
                ProjectEventContext context = new ProjectEventContext();
                context.setUsername(subscription.getUsername());
                context.setUserUUID(subscription.getUserUUID());
                context.setProjectId(sessionProjectPayload.getProjectId());
                projectObserverEvent.setContext(context);
                publisher.publishEvent(projectObserverEvent);
            }
        });

    }


    public Set<UsernameUUIDPair> getAllProjectSubscriptions(UUID projectUUID){

        Set<UsernameUUIDPair> answer = new HashSet<>();
        List<Subscription> subs = projectToSubAssosiation.get(projectUUID);
        if (subs!=null){
            subs.forEach(subscription -> {
                answer.add(new UsernameUUIDPair(subscription.getUsername(), subscription.getUserUUID()));
            });
        }

        return answer;
    }








    @Scheduled(fixedDelay = 5, timeUnit = TimeUnit.SECONDS)
    public void testStats(){



        System.out.println("Количество активных сессий: "+sessionStore.size());
        System.out.println("Количество активных проектов: "+projectToSubAssosiation.size());
        System.out.println("Количество активных юзеров: "+userToSubAssosiation.size());
    }






}
