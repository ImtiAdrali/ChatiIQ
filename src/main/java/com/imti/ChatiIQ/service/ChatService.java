package com.imti.ChatiIQ.service;

import com.imti.ChatiIQ.LLMClient;
import com.imti.ChatiIQ.dto.ChatRequest;
import com.imti.ChatiIQ.dto.ChatResponse;
import com.imti.ChatiIQ.entity.Conversation;
import com.imti.ChatiIQ.entity.Message;
import com.imti.ChatiIQ.enums.Role;
import com.imti.ChatiIQ.repository.ConversationRepository;
import com.imti.ChatiIQ.repository.MessageRepository;
import jakarta.transaction.Transactional;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {

    private final LLMClient llmClient;
    private final ChatClient chatClient;
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;

    public ChatService(LLMClient llmClient,
                       ConversationRepository conversationRepository,
                       MessageRepository messageRepository,
                       ChatClient.Builder chatClient) {
        this.llmClient = llmClient;
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.chatClient = chatClient.build();
    }

    public ChatResponse chat(Long conversionId, String userMessage) {

        int MAX_MESSAGES = 10;
        Conversation conversation = getOrCreateConversation(conversionId);

        List<Message> history = getRecentMessages(conversation.getId(), MAX_MESSAGES);

        ChatClient.ChatClientRequestSpec prompt = buildPrompt(history, userMessage);

        String aiResponse = prompt.call().content();

        saveUserMessage(conversation, userMessage);
        saveAssistantMessages(conversation, aiResponse);

        return new ChatResponse(aiResponse);
    }

    @Transactional
    public void deleteConversation(Long conversationId) {
        conversationRepository.deleteById(conversationId);
    }

    @Transactional
    public void clearConversation(Long conversationId) {
        messageRepository.deleteByConversationId(conversationId);

        Conversation convo = conversationRepository.findById(conversationId).orElseThrow();
        convo.setTitle("New Chat");
        conversationRepository.save(convo);

    }


    public Flux<String> streamChat(Long conversationId, String userMessage) {
        int MAX_MESSAGES = 10;

        Conversation conversation = getOrCreateConversation(conversationId);
        List<Message> history = getRecentMessages(conversation.getId(), MAX_MESSAGES);

        ChatClient.ChatClientRequestSpec prompt = buildPrompt(history, userMessage);

        Flux<String> responseStream = prompt
                .stream()
                .content();

       saveUserMessage(conversation, userMessage);

        StringBuilder fullResponse = new StringBuilder();
        return responseStream.doOnNext(fullResponse::append).doOnComplete(() -> {
            saveAssistantMessages(conversation, fullResponse.toString());
        });
    }

    private Conversation getOrCreateConversation(Long conversationId) {
        if (conversationId == null) {
            Conversation convo = new Conversation();
            convo.setTitle("New Chat");
            convo.setCreatedAt(LocalDateTime.now());
            return conversationRepository.save(convo);
        }
        return conversationRepository.findById(conversationId).orElseThrow();
    }

    private List<Message> getRecentMessages(Long conversationId, int maxMessages) {
        List<Message> history = messageRepository.findByConversationIdOrderByTimestampAsc(conversationId);
        if (history.size() > maxMessages) {
            return history.subList(history.size() - maxMessages, history.size());
        }
        return history;
    }

    private ChatClient.ChatClientRequestSpec buildPrompt(List<Message> history, String userMessage) {
        ChatClient.ChatClientRequestSpec prompt = chatClient.prompt();

        for (Message msg: history) {
            if (msg.getSender_role() == Role.USER) {
                prompt = prompt.user(msg.getContent());
            } else {
                prompt = prompt.system(msg.getContent());
            }
        }

        return prompt.user(userMessage);
    }

    private void saveUserMessage(Conversation conversation, String content) {
        Message msg = Message.builder()
                .sender_role(Role.USER)
                .content(content)
                .timestamp(LocalDateTime.now())
                .conversation(conversation)
                .build();
        messageRepository.save(msg);
    }

    private void saveAssistantMessages(Conversation conversation, String content) {
        Message msg = Message.builder()
                .sender_role(Role.ASSISTANT)
                .content(content)
                .timestamp(LocalDateTime.now())
                .conversation(conversation)
                .build();
        messageRepository.save(msg);
    }
}
