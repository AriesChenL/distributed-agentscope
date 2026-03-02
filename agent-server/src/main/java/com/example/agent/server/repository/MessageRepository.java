package com.example.agent.server.repository;

import com.example.agent.server.document.MessageDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息 Repository
 */
@Repository
public interface MessageRepository extends MongoRepository<MessageDocument, String> {
    
    Page<MessageDocument> findByConversationId(String conversationId, Pageable pageable);
    
    List<MessageDocument> findByConversationIdOrderByCreatedAtAsc(String conversationId);
    
    Page<MessageDocument> findByAgentId(String agentId, Pageable pageable);
    
    Page<MessageDocument> findByConversationIdAndCreatedAtBetween(
            String conversationId, 
            LocalDateTime startTime, 
            LocalDateTime endTime, 
            Pageable pageable);
    
    long countByConversationId(String conversationId);
    
    void deleteByConversationId(String conversationId);
}
