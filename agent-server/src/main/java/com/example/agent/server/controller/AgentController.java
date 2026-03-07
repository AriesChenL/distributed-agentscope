package com.example.agent.server.controller;

import com.example.agent.common.dto.ApiResponse;
import com.example.agent.server.service.AgentService;
import com.example.agent.server.service.RealAgentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Agent 控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;
    private final RealAgentService realAgentService;

    @GetMapping("/status")
    public ApiResponse<Map<String, String>> getStatus() {
        // 优先使用真正的 Agent 服务
        String status;
        if (realAgentService.isAvailable()) {
            status = realAgentService.getStatus();
        } else {
            status = agentService.getStatus();
        }
        
        Map<String, String> result = new HashMap<>();
        result.put("status", status);
        result.put("type", realAgentService.isAvailable() ? "REAL_AGENT" : "SIMULATED");

        return ApiResponse.success(result);
    }

    @PostMapping("/execute")
    public ApiResponse<String> executeTask(@RequestBody Map<String, String> request) {
        String taskName = request.get("taskName");
        if (taskName == null || taskName.isEmpty()) {
            return ApiResponse.error(400, "taskName is required");
        }

        // 优先使用真正的 Agent 服务
        String result;
        if (realAgentService.isAvailable()) {
            log.info("Using RealAgentService to execute task: {}", taskName);
            result = realAgentService.executeTask(taskName);
        } else {
            log.info("Using simulated AgentService to execute task: {}", taskName);
            result = agentService.executeTask(taskName);
        }
        
        return ApiResponse.success(result);
    }
}
