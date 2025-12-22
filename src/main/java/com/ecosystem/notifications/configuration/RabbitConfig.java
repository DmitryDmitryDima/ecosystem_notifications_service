package com.ecosystem.notifications.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Value("${users.activity_events.exchange.name}")
    private String USERS_ACTIVITY_EXCHANGE;

    @Value("${users.activity_events.queue.name}")
    private String USERS_ACTIVITY_QUEUE;

    @Value("${users.projects_events.exchange.name}")
    private String USERS_PROJECTS_EVENTS_EXCHANGE;

    @Value("${users.projects_events.queue.name}")
    private String USERS_PROJECTS_EVENTS_QUEUE;




    // users activity

    @Bean
    public Queue usersActivityQueue(){
        return new Queue(USERS_ACTIVITY_QUEUE);
    }


    @Bean
    public FanoutExchange usersActivityExchange(){
        return new FanoutExchange(USERS_ACTIVITY_EXCHANGE);
    }

    @Bean
    public Binding activityBinding(@Qualifier("usersActivityQueue") Queue activityQueue,
                           @Qualifier("usersActivityExchange") FanoutExchange activityExchange) {
        return BindingBuilder
                .bind(activityQueue)
                .to(activityExchange);

    }


    // projects activity - room based

    @Bean
    public Queue projectsEventsQueue(){
        return new Queue(USERS_PROJECTS_EVENTS_QUEUE);
    }


    @Bean
    public FanoutExchange projectsEventsExchange(){
        return new FanoutExchange(USERS_PROJECTS_EVENTS_EXCHANGE);
    }

    @Bean
    public Binding projectsBinding(@Qualifier("projectsEventsQueue") Queue projectsQueue,
                                   @Qualifier("projectsEventsExchange") FanoutExchange projectsExchange) {
        return BindingBuilder
                .bind(projectsQueue)
                .to(projectsExchange);

    }








}
