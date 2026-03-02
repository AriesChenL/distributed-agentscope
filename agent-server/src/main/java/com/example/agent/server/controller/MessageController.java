package com.example.agent.server.controller;

import com.example.agent.common.dto.ApiResponse;
import com.example.agent.server.document.MessageDocument;
import com.example.agent.server.dto.MessageRequest;
import com.example.agent.server.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息控制器
 */
@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {
    
    private final MessageService messageService;
    
    /**
     * 保存消息
     */
    @PostMapping
    public ApiResponse<MessageDocument> saveMessage(@RequestBody MessageRequest request) {
        MessageDocument message = messageService.saveMessage(request);
        return ApiResponse.success(message);
    }
    
    /**
     * 获取会话消息列表（分页）
     */
    @GetMapping("/conversation/{conversationId}")
    public ApiResponse<Page<MessageDocument>> getMessagesByConversationId(
            @PathVariable String conversationId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<MessageDocument> messages = messageService.getMessagesByConversationId(
                conversationId, page, size);
        return ApiResponse.success(messages);
    }
    
    /**
     * 获取会话所有消息
     */
    @GetMapping("/conversation/{conversationId}/all")
    public ApiResponse<List<MessageDocument>> getAllMessagesByConversationId(
            @PathVariable String conversationId) {
        List<MessageDocument> messages = messageService.getAllMessagesByConversationId(
                conversationId);
        return ApiResponse.success(messages);
    }
    
    /**
     * 获取 Agent 的消息
     */
    @GetMapping("/agent/{agentId}")
    public ApiResponse<Page<MessageDocument>> getMessagesByAgentId(
            @PathVariable String agentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<MessageDocument> messages = messageService.getMessagesByAgentId(
                agentId, page, size);
        return ApiResponse.success(messages);
    }
    
    /**
     * 按时间范围查询消息
     */
    @GetMapping("/conversation/{conversationId}/range")
    public ApiResponse<Page<MessageDocument>> getMessagesByTimeRange(
            @PathVariable String conversationId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<MessageDocument> messages = messageService.getMessagesByTimeRange(
                conversationId, startTime, endTime, page, size);
        return ApiResponse.success(messages);
    }
    
    /**
     * 获取会话消息数量
     */
    @GetMapping("/conversation/{conversationId}/count")
    public ApiResponse<Map<String, Long>> countMessagesByConversationId(
            @PathVariable String conversationId) {
        long count = messageService.countMessagesByConversationId(conversationId);
        Map<String, Long> result = new HashMap<>();
        result.put("count", count);
        return ApiResponse.success(result);
    }
    
    /**
     * 删除会话所有消息
     */
    @DeleteMapping("/conversation/{conversationId}")
    public ApiResponse<Void> deleteMessagesByConversationId(
            @PathVariable String conversationId) {
        messageService.deleteMessagesByConversationId(conversationId);
        return ApiResponse.success(null);
    }
}
