package com.example.agent.server.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.agent.server.entity.Agent;
import com.example.agent.server.mapper.AgentMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Agent 服务实现类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements IAgentService {
    
    private final AgentMapper agentMapper;
    
    @Override
    public List<Agent> listAgents() {
        log.info("Listing all agents");
        return agentMapper.selectList(new LambdaQueryWrapper<Agent>()
                .eq(Agent::getDeleted, 0));
    }
    
    @Override
    public Agent getAgentById(Long id) {
        log.info("Getting agent by id: {}", id);
        return agentMapper.selectById(id);
    }
    
    @Override
    public boolean createAgent(Agent agent) {
        log.info("Creating agent: {}", agent.getName());
        return agentMapper.insert(agent) > 0;
    }
    
    @Override
    public boolean updateAgent(Agent agent) {
        log.info("Updating agent: {}", agent.getId());
        return agentMapper.updateById(agent) > 0;
    }
    
    @Override
    public boolean deleteAgent(Long id) {
        log.info("Deleting agent: {}", id);
        return agentMapper.deleteById(id) > 0;
    }
    
    @Override
    public List<Agent> listAgentsByStatus(String status) {
        log.info("Listing agents by status: {}", status);
        return agentMapper.selectList(new LambdaQueryWrapper<Agent>()
                .eq(Agent::getStatus, status)
                .eq(Agent::getDeleted, 0));
    }
}
