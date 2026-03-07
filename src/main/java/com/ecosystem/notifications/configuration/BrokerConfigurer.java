package com.ecosystem.notifications.configuration;

import com.ecosystem.notifications.dto.observer.SecurityContext;
import com.ecosystem.notifications.service.ObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class BrokerConfigurer implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private HandshakeInterceptor handshakeInterceptor;

    @Autowired
    private ObservationService observationService;

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


    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration.addDecoratorFactory(new WebSocketHandlerDecoratorFactory() {
            @Override
            public WebSocketHandler decorate(final WebSocketHandler handler) {
                return new WebSocketHandlerDecorator(handler) {

                    @Override
                    public void afterConnectionEstablished(final WebSocketSession session) throws Exception {




                        SecurityContext securityContext = (SecurityContext) session.getAttributes().get("securityContext");
                        observationService.registerSecuredSession(session, securityContext);



                        super.afterConnectionEstablished(session);
                    }
                    @Override
                    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {


                        observationService.unregisterSecuredSession(session.getId());
                        System.out.println("connection closed !");
                        super.afterConnectionClosed(session, closeStatus);
                    }


                };
            }
        });
    }

}
