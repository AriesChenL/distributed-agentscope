package com.example.agent.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 消息请求 DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageRequest {
    
    private String conversationId;
    
    private String agentId;
    
    private String role;
    
    private String content;
    
    private Map<String, Object> metadata;
}
