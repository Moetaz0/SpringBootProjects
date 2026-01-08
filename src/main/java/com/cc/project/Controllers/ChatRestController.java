package com.cc.project.Controllers;

import com.cc.project.Entity.Message;
import com.cc.project.Service.ChatService;

import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chats")
public class ChatRestController {

    private final ChatService chatService;

    public ChatRestController(ChatService chatService) {
        this.chatService = chatService;
    }

    // conversation history between two users
    @GetMapping("/{myId}/with/{otherId}")
    public List<Map<String, Object>> getConversation(@PathVariable Long myId, @PathVariable Long otherId) {
        List<Message> msgs = chatService.getConversation(myId, otherId);
        return msgs.stream().map(this::toDto).collect(Collectors.toList());
    }

    // simple conversations list for user (last message per other user)
    @GetMapping("/{myId}/conversations")
    public List<Map<String, Object>> getConversations(@PathVariable Long myId) {
        List<Message> all = chatService.getAllForUser(myId);
        // Build a map keyed by other user id to last message
        LinkedHashMap<Long, Message> lastMap = new LinkedHashMap<>();
        for (Message m : all) {
            Long other = m.getSenderId().equals(myId) ? m.getReceiverId() : m.getSenderId();
            if (!lastMap.containsKey(other))
                lastMap.put(other, m);
        }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<Long, Message> e : lastMap.entrySet()) {
            Message m = e.getValue();
            result.add(Map.of(
                    "otherId", e.getKey(),
                    "name", "User " + e.getKey(), // replace with real user name by calling user service if you have it
                    "lastMessage", m.getContent(),
                    "lastAt", m.getTimestamp()));
        }
        return result;
    }

    private Map<String, Object> toDto(Message m) {
        return Map.of(
                "id", m.getId(),
                "senderId", m.getSenderId(),
                "receiverId", m.getReceiverId(),
                "content", m.getContent(),
                "timestamp", m.getTimestamp());
    }
}
