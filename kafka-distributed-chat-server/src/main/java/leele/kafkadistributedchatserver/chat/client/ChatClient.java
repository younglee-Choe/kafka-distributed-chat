package leele.kafkadistributedchatserver.chat.client;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ChatClient {
    private final WebClient webClient;

    public ChatClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8000").build(); // FastAPI 주소
    }

    public Mono<String> sendTopicNameToFastAPI(String topicName) {
        return webClient.post()
                .uri("/chat/topic") // FastAPI endpoint
                .bodyValue(new TopicName(topicName))
                .retrieve()
                .bodyToMono(String.class);   // 응답을 String으로 변환
    }

    @Getter
    @Setter
    @Builder
    static class TopicName {
        private String text;

        public TopicName(String text) {
            this.text = text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
