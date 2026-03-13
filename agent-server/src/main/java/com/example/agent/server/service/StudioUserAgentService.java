package com.example.agent.server.service;

import io.agentscope.core.ReActAgent;
import io.agentscope.core.message.Msg;
import io.agentscope.core.studio.StudioManager;
import io.agentscope.core.studio.StudioUserAgent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Studio 用户交互服务
 * 通过 Web UI 接收用户输入
 */
@Slf4j
@Service
@ConditionalOnProperty(prefix = "agentscope.studio", name = "enabled", havingValue = "true", matchIfMissing = false)
public class StudioUserAgentService {

    private StudioUserAgent studioUserAgent;
    private final AtomicReference<ReActAgent> agentRef = new AtomicReference<>();
    private final AtomicReference<Msg> lastMsg = new AtomicReference<>();

    @PostConstruct
    public void init() {
        log.info("Initializing StudioUserAgent...");
        
        try {
            studioUserAgent = StudioUserAgent.builder()
                    .name("User")
                    .studioClient(StudioManager.getClient())
                    .webSocketClient(StudioManager.getWebSocketClient())
                    .build();
            
            log.info("StudioUserAgent initialized successfully");
        } catch (Exception e) {
            log.error("Failed to initialize StudioUserAgent: {}", e.getMessage(), e);
        }
    }

    @PreDestroy
    public void destroy() {
        studioUserAgent = null;
    }

    /**
     * 设置 Agent 实例
     */
    public void setAgent(ReActAgent agent) {
        this.agentRef.set(agent);
        log.info("Agent registered with StudioUserAgentService");
    }

    /**
     * 从 Studio Web UI 获取用户输入并让 Agent 处理
     * @return Agent 的响应
     */
    public Msg chat() {
        if (studioUserAgent == null) {
            log.warn("StudioUserAgent not initialized");
            return null;
        }
        
        ReActAgent agent = agentRef.get();
        if (agent == null) {
            log.warn("Agent not registered");
            return null;
        }
        
        try {
            // 获取用户输入
            Msg userInput = studioUserAgent.call(lastMsg.get()).block();
            if (userInput == null || "exit".equalsIgnoreCase(userInput.getTextContent())) {
                log.info("User ended conversation");
                return null;
            }
            
            // Agent 处理
            Msg response = agent.call(userInput).block();
            lastMsg.set(response);
            return response;
        } catch (Exception e) {
            log.error("Error in chat: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 检查 Studio 用户代理是否可用
     */
    public boolean isAvailable() {
        return studioUserAgent != null && agentRef.get() != null;
    }
}
