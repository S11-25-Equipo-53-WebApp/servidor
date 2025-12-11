package com.webapp.backend.websocket.config;

import com.webapp.backend.infra.security.TokenService;
import com.webapp.backend.infra.security.userDetails.UserDetailsImpl;
import com.webapp.backend.infra.security.userDetails.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class WebSocketSecurityConfig implements ChannelInterceptor {

    private final TokenService tokenService;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor == null) {
            return message;
        }

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            log.info("WS CONNECT attempt...");

            String authHeader = accessor.getFirstNativeHeader("Authorization");

            // Token desde handshake (ws?token=xxx)
            if (authHeader == null && accessor.getSessionAttributes() != null) {
                Object tokenAttr = accessor.getSessionAttributes().get("token");
                if (tokenAttr != null) {
                    authHeader = "Bearer " + tokenAttr;
                }
            }

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.error("WS connection denied: Missing or invalid Authorization header");
                throw new IllegalArgumentException("Missing or invalid WebSocket token");
            }

            String token = authHeader.substring(7);

            try {
                String email = tokenService.getSubject(token);
                log.info("WS Auth OK for user={}", email);

                UserDetailsImpl userDetails =
                        (UserDetailsImpl) userDetailsService.loadUserByUsername(email);

                var auth = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                accessor.setUser(auth);

            } catch (Exception e) {
                log.error("WS token validation failed: {}", e.getMessage());
                throw new IllegalArgumentException("Invalid WebSocket token: " + e.getMessage());
            }
        }

        return message;
    }
}
