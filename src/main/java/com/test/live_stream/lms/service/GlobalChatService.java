package com.test.live_stream.lms.service;

import com.test.live_stream.lms.dto.ChatMessageRequest;
import com.test.live_stream.lms.dto.ChatMessageResponse;
import com.test.live_stream.lms.dto.ChatPartnerResponse;
import com.test.live_stream.lms.model.GlobalChatMessage;
import com.test.live_stream.lms.model.User;
import com.test.live_stream.lms.repository.GlobalChatMessageRepository;
import com.test.live_stream.lms.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GlobalChatService {

    private final GlobalChatMessageRepository messageRepository;
    private final UserRepository userRepository;

    public GlobalChatService(GlobalChatMessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public ChatMessageResponse sendMessage(String senderId, ChatMessageRequest request) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        GlobalChatMessage msg = new GlobalChatMessage();
        msg.setSender(sender);
        msg.setReceiver(receiver);
        msg.setMessageText(request.getMessageText());
        messageRepository.save(msg);

        return toResponse(msg);
    }

    public List<ChatMessageResponse> getConversation(String currentUserId, String otherUserId) {
        return messageRepository.findConversation(currentUserId, otherUserId)
                .stream().map(this::toResponse).toList();
    }

    public List<ChatPartnerResponse> getChatPartners(String userId) {
        return messageRepository.findChatPartners(userId).stream()
                .map(u -> new ChatPartnerResponse(u.getUserId(), u.getFullName(), u.getEmail(), u.getUserType().name()))
                .toList();
    }

    public List<ChatPartnerResponse> searchUsers(String query, String excludeUserId) {
        // Simple search by name or email containing the query string
        return userRepository.findAll().stream()
                .filter(u -> !u.getUserId().equals(excludeUserId))
                .filter(u -> u.getFullName().toLowerCase().contains(query.toLowerCase()) ||
                        u.getEmail().toLowerCase().contains(query.toLowerCase()))
                .limit(20)
                .map(u -> new ChatPartnerResponse(u.getUserId(), u.getFullName(), u.getEmail(), u.getUserType().name()))
                .toList();
    }

    private ChatMessageResponse toResponse(GlobalChatMessage msg) {
        return new ChatMessageResponse(
                msg.getMessageId(),
                msg.getSender().getUserId(),
                msg.getSender().getFullName(),
                msg.getReceiver().getUserId(),
                msg.getReceiver().getFullName(),
                msg.getMessageText(),
                msg.getTimestamp());
    }
}
