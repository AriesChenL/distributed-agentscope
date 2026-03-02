package com.example.agent.server.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * AgentService 测试
 */
@SpringBootTest
class AgentServiceTest {
    
    @Autowired
    private AgentService agentService;
    
    @Test
    void testGetStatus() {
        String status = agentService.getStatus();
        assertEquals("ACTIVE", status);
    }
    
    @Test
    void testExecuteTask() {
        String taskName = "test-task";
        String result = agentService.executeTask(taskName);
        
        assertTrue(result.contains(taskName));
        assertTrue(result.contains("executed successfully"));
    }
}
