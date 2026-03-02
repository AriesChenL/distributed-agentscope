package com.example.agent.server.service;

import com.example.agent.server.entity.Agent;

import java.util.List;

/**
 * Agent 服务接口
 */
public interface IAgentService {
    
    List<Agent> listAgents();
    
    Agent getAgentById(Long id);
    
    boolean createAgent(Agent agent);
    
    boolean updateAgent(Agent agent);
    
    boolean deleteAgent(Long id);
    
    List<Agent> listAgentsByStatus(String status);
}
