package com.example.agent.server.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 消息文档 - 存储在 MongoDB
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "messages")
public class MessageDocument {
    
    @Id
    private String id;
    
    @Indexed
    private String conversationId;
    
    @Indexed
    private String agentId;
    
    private String role;  // USER, ASSISTANT, SYSTEM
    
    private String content;
    
    private Map<String, Object> metadata;
    
    @CreatedDate
    private LocalDateTime createdAt;
}
