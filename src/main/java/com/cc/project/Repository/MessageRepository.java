package com.cc.project.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cc.project.Entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    // fetch conversation between two users ordered by timestamp
    @Query("select m from Message m where (m.senderId = ?1 and m.receiverId = ?2) or (m.senderId = ?2 and m.receiverId = ?1) order by m.timestamp")
    List<Message> findConversation(Long userA, Long userB);

    // quick conversation list for a user (latest per other) — simple approach
    @Query("select m from Message m where m.senderId = ?1 or m.receiverId = ?1 order by m.timestamp desc")
    List<Message> findAllForUser(Long userId);
}
