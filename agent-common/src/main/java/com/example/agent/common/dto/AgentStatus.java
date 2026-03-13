package com.example.agent.common.dto;

/**
 * Agent 状态常量类
 */
public class AgentStatus {

    public static final String ACTIVE = "ACTIVE";
    public static final String INACTIVE = "INACTIVE";
    public static final String BUSY = "BUSY";
    public static final String NOT_INITIALIZED = "NOT_INITIALIZED";
    public static final String NOT_AVAILABLE = "NOT_AVAILABLE";

    private AgentStatus() {
        // 私有构造器，防止实例化
    }
}
