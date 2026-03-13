package com.example.agent.server.controller;

import com.example.agent.common.dto.ApiResponse;
import com.example.agent.server.service.StudioUserAgentService;
import io.agentscope.core.message.Msg;
import io.agentscope.core.studio.StudioManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Studio 控制器
 * 提供与 AgentScope Studio 交互的 API
 */
@Slf4j
@RestController
@RequestMapping("/api/studio")
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "agentscope.studio", name = "enabled", havingValue = "true", matchIfMissing = false)
public class StudioController {

    private final StudioUserAgentService studioUserAgentService;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    /**
     * 获取 Studio 连接状态
     */
    @GetMapping("/status")
    public ApiResponse<Map<String, Object>> getStudioStatus() {
        Map<String, Object> result = new HashMap<>();
        
        boolean studioInitialized = StudioManager.isInitialized();
        boolean userAgentAvailable = studioUserAgentService.isAvailable();
        
        result.put("studioInitialized", studioInitialized);
        result.put("userAgentAvailable", userAgentAvailable);
        
        return ApiResponse.success(result);
    }

    /**
     * 启动对话循环（在后台运行）
     */
    @PostMapping("/chat/start")
    public ApiResponse<Map<String, String>> startChat() {
        if (!studioUserAgentService.isAvailable()) {
            return ApiResponse.error(503, "Studio not available or Agent not registered");
        }

        // 在后台启动对话循环
        CompletableFuture.runAsync(() -> {
            log.info("Starting chat loop...");
            int turn = 1;
            while (true) {
                log.info("[Turn {}] Waiting for user input...", turn);
                Msg response = studioUserAgentService.chat();
                if (response == null) {
                    log.info("Chat ended");
                    break;
                }
                log.info("[Turn {}] Agent: {}", turn, response.getTextContent());
                turn++;
            }
        }, executor);

        Map<String, String> result = new HashMap<>();
        result.put("message", "Chat loop started. Open Studio Web UI to interact.");
        
        return ApiResponse.success(result);
    }

    /**
     * 停止对话循环
     */
    @PostMapping("/chat/stop")
    public ApiResponse<Map<String, String>> stopChat() {
        // 通过发送退出信号来停止
        Map<String, String> result = new HashMap<>();
        result.put("message", "Chat stop requested. Send 'exit' in Studio to end conversation.");
        return ApiResponse.success(result);
    }
}
