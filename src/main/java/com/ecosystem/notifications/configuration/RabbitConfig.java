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

    @Value("${system.projects_events.exchange.name}")
    private String SYSTEM_PROJECTS_EVENTS_EXCHANGE;

    @Value("${system.projects_events.queue.name}")
    private String SYSTEM_PROJECTS_EVENTS_QUEUE;




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


    // users projects activity - room based

    @Bean
    public Queue usersProjectsEventsQueue(){
        return new Queue(USERS_PROJECTS_EVENTS_QUEUE);
    }


    @Bean
    public FanoutExchange usersProjectsEventsExchange(){
        return new FanoutExchange(USERS_PROJECTS_EVENTS_EXCHANGE);
    }

    @Bean
    public Binding usersProjectsBinding(@Qualifier("usersProjectsEventsQueue") Queue projectsQueue,
                                        @Qualifier("usersProjectsEventsExchange") FanoutExchange projectsExchange) {
        return BindingBuilder
                .bind(projectsQueue)
                .to(projectsExchange);

    }

    // system projects events
    @Bean
    public Queue systemProjectsEventsQueue() {return new Queue(SYSTEM_PROJECTS_EVENTS_QUEUE);}

    @Bean
    public FanoutExchange systemProjectsEventsExchange(){
        return new FanoutExchange(SYSTEM_PROJECTS_EVENTS_EXCHANGE);
    }

    @Bean
    public Binding systemProjectsEventsBinding(@Qualifier("systemProjectsEventsQueue") Queue systemProjectsQueue,
                                        @Qualifier("systemProjectsEventsExchange") FanoutExchange exchange) {
        return BindingBuilder
                .bind(systemProjectsQueue)
                .to(exchange);

    }








}
