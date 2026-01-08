package com.cc.project.Controllers;

import com.cc.project.Entity.ChatMessage;
import com.cc.project.Entity.Message;
import com.cc.project.Service.ChatService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
public class ChatController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChatService chatService;

    public ChatController(SimpMessagingTemplate messagingTemplate, ChatService chatService) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
    }

    @MessageMapping("/chat.send")
    public void receiveMessage(ChatMessage dto) {
        // build entity and persist
        Message m = new Message();
        m.setSenderId(dto.getSenderId());
        m.setReceiverId(dto.getReceiverId());
        m.setContent(dto.getContent());
        m.setTimestamp(Instant.now());
        Message saved = chatService.saveMessage(m);

        // Create outgoing DTO
        ChatMessage out = new ChatMessage();
        out.setId(saved.getId());
        out.setSenderId(saved.getSenderId());
        out.setReceiverId(saved.getReceiverId());
        out.setContent(saved.getContent());
        out.setTimestamp(saved.getTimestamp().toString());

        // Send to receiver's personal destination
        messagingTemplate.convertAndSendToUser(String.valueOf(saved.getReceiverId()),
                "/queue/messages", out);

        // Also send a copy back to sender's personal destination (so sender gets
        // confirmation)
        messagingTemplate.convertAndSendToUser(String.valueOf(saved.getSenderId()),
                "/queue/messages", out);

        // Optional: broadcast to /topic/messages for admin monitoring
        messagingTemplate.convertAndSend("/topic/messages", out);
    }
}
