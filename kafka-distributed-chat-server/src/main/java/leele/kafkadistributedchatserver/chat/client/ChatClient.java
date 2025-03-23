package leele.kafkadistributedchatserver.chat.client;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class ChatClient {
    private final WebClient webClient;
    private String topicName;

    public ChatClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:8000").build(); // FastAPI 주소
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
        sendTopicNameToFastAPI(topicName).subscribe(
                response -> System.out.println("✅FastAPI Response: " + response),
                error -> System.err.println("❌FastAPI Transmission Error: " + error.getMessage())
        );
    }

    public String getTopicName() {
        return topicName;
    }

    public Mono<String> sendTopicNameToFastAPI(String topicName) {
        return webClient.post()
                .uri("/chat/topic") // FastAPI endpoint
                .bodyValue(Map.of("topicName", topicName))
                .retrieve()
                .bodyToMono(String.class);   // 응답을 String으로 변환
    }
}
