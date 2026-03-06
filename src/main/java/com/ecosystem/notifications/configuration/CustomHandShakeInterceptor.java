package com.ecosystem.notifications.configuration;

import com.ecosystem.notifications.dto.observer.SecurityContext;
import org.jspecify.annotations.Nullable;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
// в данном компоненте мы сохраняем контекст
@Component
public class CustomHandShakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) throws Exception {
        //System.out.println("headers");
        //System.out.println(request.getHeaders());
        //System.out.println(request.getHeaders().toSingleValueMap());

        try {
            // теперь вся сессия будет иметь доступ к контексту. включая сопряженные с ней ивенты
            SecurityContext context = SecurityContext.generateContext(request.getHeaders().toSingleValueMap());
            attributes.put("securityContext", context);

            System.out.println("handshake");





            System.out.println(context);
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception exception) {

    }
}
