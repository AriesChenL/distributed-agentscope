package com.example.agent.server.controller;

import com.example.agent.common.dto.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 */
@RestController
@RequestMapping("/api")
public class HealthController {
    
    @GetMapping("/health")
    public ApiResponse<Map<String, Object>> health() {
        Map<String, Object> healthInfo = new HashMap<>();
        healthInfo.put("status", "UP");
        healthInfo.put("service", "agent-server");
        healthInfo.put("timestamp", System.currentTimeMillis());
        
        return ApiResponse.success(healthInfo);
    }
    
    @GetMapping("/info")
    public ApiResponse<Map<String, String>> info() {
        Map<String, String> info = new HashMap<>();
        info.put("name", "Distributed AgentScope Server");
        info.put("version", "1.0.0-SNAPSHOT");
        info.put("description", "Agent Server for Distributed AgentScope");
        
        return ApiResponse.success(info);
    }
}
