package com.example.agent.server.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.agent.server.entity.Agent;
import org.apache.ibatis.annotations.Mapper;

/**
 * Agent Mapper 接口
 */
@Mapper
public interface AgentMapper extends BaseMapper<Agent> {
    
}
