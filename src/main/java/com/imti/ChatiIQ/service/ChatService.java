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
        Conversation conversation;

        if (conversionId == null) {
            conversation = new Conversation();
            conversation.setTitle("New Chat");
            conversation.setCreatedAt(LocalDateTime.now());
            conversation = conversationRepository.save(conversation);
        } else {
            conversation = conversationRepository.findById(conversionId).orElseThrow();
        }

        List<Message> history = messageRepository.findByConversationIdOrderByTimestampAsc(conversation.getId());

        if (history.size() > MAX_MESSAGES) {
            history = history.subList(history.size() - MAX_MESSAGES, history.size());
        }

        ChatClient.ChatClientRequestSpec prompt = chatClient.prompt();
        for (Message msg: history) {
            if (msg.getSender_role() == Role.USER) {
                prompt = prompt.user(msg.getContent());
            } else {
                prompt = prompt.system(msg.getContent());
            }
        }

        prompt = prompt.user(userMessage);

        String aiResponse = prompt.call().content();

        Message userMsg = new Message();
        userMsg.setSender_role(Role.USER);
        userMsg.setContent(userMessage);
        userMsg.setTimestamp(LocalDateTime.now());
        userMsg.setConversation(conversation);
        messageRepository.save(userMsg);

        Message aiMsg = new Message();
        aiMsg.setSender_role(Role.ASSISTANT);
        aiMsg.setContent(aiResponse);
        aiMsg.setTimestamp(LocalDateTime.now());
        aiMsg.setConversation(conversation);
        messageRepository.save(aiMsg);


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



}
