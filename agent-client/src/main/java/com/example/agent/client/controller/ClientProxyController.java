package com.example.agent.client.controller;

import com.example.agent.client.service.AgentClientService;
import com.example.agent.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * 客户端代理控制器 - 调用服务端
 */
@RestController
@RequestMapping("/api/proxy")
@RequiredArgsConstructor
public class ClientProxyController {
    
    private final AgentClientService agentClientService;
    
    @GetMapping("/health")
    public Mono<ApiResponse<Map<String, Object>>> getServerHealth() {
        return agentClientService.getServerHealth();
    }
    
    @PostMapping("/execute")
    public Mono<ApiResponse<String>> executeTask(@RequestBody Map<String, String> request) {
        String taskName = request.get("taskName");
        if (taskName == null || taskName.isEmpty()) {
            return Mono.just(ApiResponse.error(400, "taskName is required"));
        }
        return agentClientService.executeTaskOnServer(taskName);
    }
}
