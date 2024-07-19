package com.dizzycode.dizzycode.interceptor;

import com.dizzycode.dizzycode.service.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private final JWTUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        String token = servletRequest.getParameter("token");

        if (token == null || jwtUtil.isExpired(token)) {
            response.setStatusCode(HttpStatus.FORBIDDEN);
            return false;
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        HttpServletRequest servletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        String token = servletRequest.getParameter("token");

        if (token != null && !jwtUtil.isExpired(token)) {
            // websocket 연결 후 online으로 접속상태 변경
            Long memberId = jwtUtil.getMemberId(token);
            LocalDateTime now = LocalDateTime.now();

            // Redis에 사용자 상태 저장
            Map<String, String> userStatus = new HashMap<>();
            userStatus.put("status", "online");
            userStatus.put("lastActive", now.toString());

            // userStatus 맵을 "user:{memberId}" 해시에 저장 (기존 값이 있다면 업데이트)
            redisTemplate.opsForHash().putAll("member:" + memberId, userStatus);
        }
    }
}
