package com.cc.project.Service;

import com.cc.project.Entity.Message;
import com.cc.project.Repository.MessageRepository;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ChatService {
    private final MessageRepository repo;

    public ChatService(MessageRepository repo) {
        this.repo = repo;
    }

    public Message saveMessage(Message m) {
        return repo.save(m);
    }

    public List<Message> getConversation(Long a, Long b) {
        return repo.findConversation(a, b);
    }

    public List<Message> getAllForUser(Long userId) {
        return repo.findAllForUser(userId);
    }
}
