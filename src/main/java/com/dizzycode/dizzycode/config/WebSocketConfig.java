package com.dizzycode.dizzycode.config;

import com.dizzycode.dizzycode.interceptor.WebSocketHandshakeInterceptor;
import com.dizzycode.dizzycode.common.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Value("${spring.rabbitmq.host}")
    private String rabbitmqHost;
    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Use RabbitMQ as the message broker
        config.enableStompBrokerRelay("/topic")
                .setRelayHost(rabbitmqHost) // Docker Compose 네트워크에서 RabbitMQ 컨테이너의 이름 사용
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");
        config.setApplicationDestinationPrefixes("/app");
        config.setPathMatcher(new AntPathMatcher("."));
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws/gs-guide-websocket")
                .addInterceptors(new WebSocketHandshakeInterceptor(jwtUtil, redisTemplate)) // Add interceptor here
                .setAllowedOrigins("http://localhost:5173")
                .withSockJS();
    }
}
