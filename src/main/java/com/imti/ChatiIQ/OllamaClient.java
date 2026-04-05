package com.imti.ChatiIQ;

import com.imti.ChatiIQ.dto.ChatResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class OllamaClient implements LLMClient{

    private final ChatClient chatClient;

    public OllamaClient(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public String sendMessage(String message) {
            String response = chatClient.prompt()
                    .user(message)
                    .call()
                    .content();

        assert response != null;
        return response;
    }
}
