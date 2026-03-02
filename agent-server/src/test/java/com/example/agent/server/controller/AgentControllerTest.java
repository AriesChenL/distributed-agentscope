package com.example.agent.server.controller;

import com.example.agent.common.dto.ApiResponse;
import com.example.agent.server.service.AgentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * AgentController 测试
 */
@WebMvcTest(AgentController.class)
class AgentControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private AgentService agentService;
    
    @Test
    void testGetStatus() throws Exception {
        when(agentService.getStatus()).thenReturn("ACTIVE");
        
        mockMvc.perform(get("/api/agent/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.status").value("ACTIVE"));
    }
    
    @Test
    void testExecuteTask() throws Exception {
        when(agentService.executeTask(anyString())).thenReturn("Task 'test' executed successfully");
        
        mockMvc.perform(post("/api/agent/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskName\":\"test\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value("Task 'test' executed successfully"));
    }
    
    @Test
    void testExecuteTaskWithEmptyName() throws Exception {
        mockMvc.perform(post("/api/agent/execute")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"taskName\":\"\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400));
    }
}
