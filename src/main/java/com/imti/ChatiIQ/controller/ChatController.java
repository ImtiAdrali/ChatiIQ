package com.imti.ChatiIQ.controller;

import com.imti.ChatiIQ.dto.ChatRequest;
import com.imti.ChatiIQ.dto.ChatResponse;
import com.imti.ChatiIQ.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestParam(required = false) Long conversationId,
            @RequestBody ChatRequest chatRequest) {
        ChatResponse response = chatService.chat(conversationId, chatRequest.getMessage());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{conversationId}")
    public ResponseEntity<Void> deleteConversation(@PathVariable Long conversationId) {
        chatService.deleteConversation(conversationId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{conversationId}/messages")
    public ResponseEntity<Void> clearConversation(@PathVariable Long conversationId) {
        chatService.clearConversation(conversationId);
        return ResponseEntity.noContent().build();
    }


}
