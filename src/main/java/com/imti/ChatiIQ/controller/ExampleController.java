package com.imti.ChatiIQ.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleController {

    private final ChatClient chatClient;

    public ExampleController(ChatClient.Builder chatClientBuilder) {
        chatClient = chatClientBuilder.build();
    }

    @PostMapping("/ai")
    public String generation(@RequestBody String userInput) {
        return this.chatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }

}
