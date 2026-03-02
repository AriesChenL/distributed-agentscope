package com.example.agent.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Agent 服务接口
 */
@Slf4j
@Service
public class AgentService {
    
    public String getStatus() {
        log.debug("Getting agent status");
        return "ACTIVE";
    }
    
    public String executeTask(String taskName) {
        log.info("Executing task: {}", taskName);
        return "Task '" + taskName + "' executed successfully";
    }
}
