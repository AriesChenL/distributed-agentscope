package com.example.agent.client.service;

import com.example.agent.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * Agent 客户端服务 - 调用服务端 API
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AgentClientService {
    
    private final WebClient webClient;
    
    @SuppressWarnings("unchecked")
    public Mono<ApiResponse<Map<String, Object>>> getServerHealth() {
        log.info("Getting server health status");
        return webClient.get()
                .uri("/api/health")
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .map(response -> {
                    response.setData((Map<String, Object>) response.getData());
                    return response;
                });
    }
    
    @SuppressWarnings("unchecked")
    public Mono<ApiResponse<String>> executeTaskOnServer(String taskName) {
        log.info("Executing task on server: {}", taskName);
        return webClient.post()
                .uri("/api/agent/execute")
                .bodyValue(Map.of("taskName", taskName))
                .retrieve()
                .bodyToMono(ApiResponse.class)
                .map(response -> {
                    response.setData((String) response.getData());
                    return response;
                });
    }
}
