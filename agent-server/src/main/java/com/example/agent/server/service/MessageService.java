package com.example.agent.server.service;

import com.example.agent.server.document.MessageDocument;
import com.example.agent.server.dto.MessageRequest;
import com.example.agent.server.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 消息服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    
    private final MessageRepository messageRepository;
    
    /**
     * 保存消息
     */
    public MessageDocument saveMessage(MessageRequest request) {
        log.info("Saving message for conversation: {}, agent: {}", 
                request.getConversationId(), request.getAgentId());
        
        MessageDocument message = MessageDocument.builder()
                .conversationId(request.getConversationId())
                .agentId(request.getAgentId())
                .role(request.getRole())
                .content(request.getContent())
                .metadata(request.getMetadata())
                .build();
        
        return messageRepository.save(message);
    }
    
    /**
     * 获取会话消息列表
     */
    public Page<MessageDocument> getMessagesByConversationId(
            String conversationId, int page, int size) {
        log.info("Getting messages for conversation: {}", conversationId);
        return messageRepository.findByConversationId(
                conversationId, 
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt")));
    }
    
    /**
     * 获取会话所有消息（按时间排序）
     */
    public List<MessageDocument> getAllMessagesByConversationId(String conversationId) {
        log.info("Getting all messages for conversation: {}", conversationId);
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId);
    }
    
    /**
     * 获取 Agent 的消息
     */
    public Page<MessageDocument> getMessagesByAgentId(
            String agentId, int page, int size) {
        log.info("Getting messages for agent: {}", agentId);
        return messageRepository.findByAgentId(
                agentId, 
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt")));
    }
    
    /**
     * 按时间范围查询消息
     */
    public Page<MessageDocument> getMessagesByTimeRange(
            String conversationId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            int page,
            int size) {
        log.info("Getting messages by time range for conversation: {}", conversationId);
        return messageRepository.findByConversationIdAndCreatedAtBetween(
                conversationId,
                startTime,
                endTime,
                PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt")));
    }
    
    /**
     * 获取会话消息数量
     */
    public long countMessagesByConversationId(String conversationId) {
        log.info("Counting messages for conversation: {}", conversationId);
        return messageRepository.countByConversationId(conversationId);
    }
    
    /**
     * 删除会话所有消息
     */
    public void deleteMessagesByConversationId(String conversationId) {
        log.info("Deleting messages for conversation: {}", conversationId);
        messageRepository.deleteByConversationId(conversationId);
    }
}
