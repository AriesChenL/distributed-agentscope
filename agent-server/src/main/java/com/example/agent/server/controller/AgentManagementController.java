package com.example.agent.server.controller;

import com.example.agent.common.dto.ApiResponse;
import com.example.agent.server.entity.Agent;
import com.example.agent.server.service.IAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Agent 管理控制器
 */
@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentManagementController {
    
    private final IAgentService agentService;
    
    @GetMapping
    public ApiResponse<List<Agent>> listAgents() {
        List<Agent> agents = agentService.listAgents();
        return ApiResponse.success(agents);
    }
    
    @GetMapping("/{id}")
    public ApiResponse<Agent> getAgentById(@PathVariable Long id) {
        Agent agent = agentService.getAgentById(id);
        if (agent == null) {
            return ApiResponse.error(404, "Agent not found");
        }
        return ApiResponse.success(agent);
    }
    
    @PostMapping
    public ApiResponse<Boolean> createAgent(@RequestBody Agent agent) {
        boolean result = agentService.createAgent(agent);
        return ApiResponse.success(result);
    }
    
    @PutMapping("/{id}")
    public ApiResponse<Boolean> updateAgent(@PathVariable Long id, @RequestBody Agent agent) {
        agent.setId(id);
        boolean result = agentService.updateAgent(agent);
        return ApiResponse.success(result);
    }
    
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteAgent(@PathVariable Long id) {
        boolean result = agentService.deleteAgent(id);
        return ApiResponse.success(result);
    }
    
    @GetMapping("/status/{status}")
    public ApiResponse<List<Agent>> listAgentsByStatus(@PathVariable String status) {
        List<Agent> agents = agentService.listAgentsByStatus(status);
        return ApiResponse.success(agents);
    }
}
