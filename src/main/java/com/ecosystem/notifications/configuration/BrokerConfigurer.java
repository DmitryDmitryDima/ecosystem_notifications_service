package com.ecosystem.notifications.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class BrokerConfigurer implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private HandshakeInterceptor handshakeInterceptor;

    /*
    ввожу различие между публичным и приватным каналом подписки для юзер канала
    ввожу унификацию для каналов всех проектов в системе, БЕЗ УКАЗАНИЯ ЯЗЫКА
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/users/activity/private","/users/activity/public",
                "/projects"); // сюда клиент подписывается для получения персональных сообщений
        // сюда клиент подписывается для сообщений, ассоциированных с проектом
        registry.setApplicationDestinationPrefixes("/realtime"); // на адрес с этим префиксом клиент отправляет ивенты
        // (пока что в архитектуре отсутствуют действия, инициируемые websocket сообщением)


    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws/notifications")

                .addInterceptors(handshakeInterceptor)
                .setAllowedOriginPatterns("*");//  сюда мы подключаемся (gateway перенаправляет сюда)



    }

}
