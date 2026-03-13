package com.example.agent.server.config;

import com.example.agent.server.service.StudioUserAgentService;
import io.agentscope.core.message.Msg;
import io.agentscope.core.studio.StudioManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * AgentScope Studio 配置
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "agentscope.studio", name = "enabled", havingValue = "true", matchIfMissing = false)
public class StudioConfig {

    private final StudioUserAgentService studioUserAgentService;

    @Value("${agentscope.studio.url:http://localhost:3000}")
    private String studioUrl;

    @Value("${agentscope.studio.project:DistributedAgentScope}")
    private String projectName;

    @Value("${agentscope.studio.auto-start-chat:true}")
    private boolean autoStartChat;

    @Value("${spring.application.name:agent-server}")
    private String applicationName;

    private boolean initialized = false;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    @PostConstruct
    public void init() {
        log.info("Initializing AgentScope Studio connection to {}", studioUrl);
        
        try {
            StudioManager.init()
                    .studioUrl(studioUrl)
                    .project(projectName)
                    .runName(applicationName + "_" + System.currentTimeMillis())
                    .initialize()
                    .block();
            
            initialized = true;
            log.info("Successfully connected to AgentScope Studio");
            
            // 自动启动对话循环
            if (autoStartChat) {
                startChatLoop();
            }
        } catch (Exception e) {
            log.error("Failed to connect to AgentScope Studio: {}", e.getMessage(), e);
        }
    }

    /**
     * 启动后台对话循环
     */
    private void startChatLoop() {
        CompletableFuture.runAsync(() -> {
            log.info("Starting chat loop...");
            int turn = 1;
            while (true) {
                log.info("[Turn {}] Waiting for user input...", turn);
                Msg response = studioUserAgentService.chat();
                if (response == null) {
                    log.info("Chat ended by user");
                    break;
                }
                log.info("[Turn {}] Agent response sent to Studio", turn);
                turn++;
            }
        }, executor);
        log.info("Chat loop started in background. Open Studio Web UI to interact.");
    }

    @PreDestroy
    public void destroy() {
        if (initialized) {
            log.info("Shutting down AgentScope Studio connection...");
            StudioManager.shutdown();
            executor.shutdown();
            log.info("AgentScope Studio connection closed");
        }
    }

    public boolean isInitialized() {
        return initialized;
    }
}
