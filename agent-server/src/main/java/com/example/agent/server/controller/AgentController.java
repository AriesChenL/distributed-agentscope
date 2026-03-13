package com.example.agent.server.controller;

import com.example.agent.common.dto.AgentStatus;
import com.example.agent.common.dto.ApiResponse;
import com.example.agent.server.service.AgentService;
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

    @GetMapping("/status")
    public ApiResponse<Map<String, String>> getStatus() {
        String status = agentService.isAvailable() ? agentService.getStatus() : AgentStatus.NOT_AVAILABLE;

        Map<String, String> result = new HashMap<>();
        result.put("status", status);
        result.put("type", agentService.isAvailable() ? "REAL_AGENT" : AgentStatus.NOT_AVAILABLE);

        return ApiResponse.success(result);
    }

    @PostMapping("/execute")
    public ApiResponse<String> executeTask(@RequestBody Map<String, String> request) {
        String taskName = request.get("taskName");
        if (taskName == null || taskName.isEmpty()) {
            return ApiResponse.error(400, "taskName is required");
        }

        if (!agentService.isAvailable()) {
            return ApiResponse.error(503, "Agent service not available");
        }

        log.info("Using RealAgentService to execute task: {}", taskName);
        String result = agentService.executeTask(taskName);

        return ApiResponse.success(result);
    }
}
