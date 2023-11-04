package leele.kafkadistributedchatserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;

@Configuration
@EnableWebSocketMessageBroker   // STOMP 활성화
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Value("${client.origin}")
    private String origin;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins(origin);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");    // 클라이언트에게 메시지 보낼 수 있도록 Simple Broker 등록 ("/queue")
        registry.setApplicationDestinationPrefixes("/pub");          // @MessageMapping이 붙은 method로 바운드됨 ("/topic")
    }
}