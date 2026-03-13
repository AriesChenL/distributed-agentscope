package com.example.agent.server.service;

import com.example.agent.common.dto.AgentStatus;
import com.example.agent.server.tool.CalculatorTools;
import com.example.agent.server.tool.TimeTools;
import com.example.agent.server.tool.WeatherTools;
import io.agentscope.core.ReActAgent;
import io.agentscope.core.message.Msg;
import io.agentscope.core.model.*;
import io.agentscope.core.studio.StudioMessageHook;
import io.agentscope.core.studio.StudioManager;
import io.agentscope.core.tool.Toolkit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

/**
 * 真正的 Agent 服务实现（基于 AgentScope Java）
 * 支持多种模型提供商：Ollama, OpenAI, Anthropic, Gemini, DashScope
 */
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "agentscope", name = "enabled", havingValue = "true", matchIfMissing = false)
public class AgentService {

    private final TimeTools timeTools;
    private final CalculatorTools calculatorTools;
    private final WeatherTools weatherTools;
    private final StudioUserAgentService studioUserAgentService;

    @Value("${agentscope.provider:ollama}")
    private String provider;

    @Value("${agentscope.ollama.base-url:http://localhost:11434}")
    private String ollamaBaseUrl;

    @Value("${agentscope.ollama.model-name:qwen3-max}")
    private String ollamaModelName;

    @Value("${agentscope.openai.api-key:}")
    private String openaiApiKey;

    @Value("${agentscope.openai.base-url:https://api.openai.com/v1}")
    private String openaiBaseUrl;

    @Value("${agentscope.openai.model-name:gpt-4o}")
    private String openaiModelName;

    @Value("${agentscope.anthropic.api-key:}")
    private String anthropicApiKey;

    @Value("${agentscope.anthropic.model-name:claude-sonnet-4-5-20250929}")
    private String anthropicModelName;

    @Value("${agentscope.gemini.api-key:}")
    private String geminiApiKey;

    @Value("${agentscope.gemini.model-name:gemini-2.5-flash}")
    private String geminiModelName;

    @Value("${agentscope.dashscope.api-key:}")
    private String dashscopeApiKey;

    @Value("${agentscope.dashscope.model-name:qwen3-max}")
    private String dashscopeModelName;

    private ReActAgent agent;
    private String currentModel;

    @PostConstruct
    public void init() {
        log.info("Initializing RealAgentService with AgentScope Java, provider: {}", provider);

        // 创建工具包
        Toolkit toolkit = new Toolkit();
        toolkit.registerTool(timeTools);
        toolkit.registerTool(calculatorTools);
        toolkit.registerTool(weatherTools);

        log.info("Registered tools: {}", toolkit.getToolNames());

        // 根据提供商创建模型
        try {
            ChatModelBase model = createModel();
            if (model == null) {
                log.warn("No valid API key configured for provider: {}", provider);
                return;
            }

            // 创建 ReAct Agent
            var builder = ReActAgent.builder()
                    .name("AgentScope-Assistant")
                    .sysPrompt("你是一个智能 AI 助手，名为 Jarvis。你可以帮助用户查询天气、计算数学表达式、获取时间信息等。" +
                            "请使用提供的工具来完成任务。如果工具无法完成任务，请礼貌地告知用户。")
                    .toolkit(toolkit)
                    .maxIters(10);

            // 如果 Studio 已初始化，添加 Studio Hook
            if (StudioManager.isInitialized()) {
                builder.hook(new StudioMessageHook(StudioManager.getClient()));
                log.info("StudioMessageHook added to Agent");
            }

            // 根据模型类型调用不同的 model 方法
            if (model instanceof OllamaChatModel ollamaModel) {
                builder.model(ollamaModel);
            } else if (model instanceof OpenAIChatModel openaiModel) {
                builder.model(openaiModel);
            } else if (model instanceof AnthropicChatModel anthropicModel) {
                builder.model(anthropicModel);
            } else if (model instanceof GeminiChatModel geminiModel) {
                builder.model(geminiModel);
            } else if (model instanceof DashScopeChatModel dashscopeModel) {
                builder.model(dashscopeModel);
            }

            agent = builder.build();

            // 如果 Studio 可用，注册 Agent
            if (studioUserAgentService != null) {
                studioUserAgentService.setAgent(agent);
            }

            log.info("ReAct Agent initialized successfully with model: {}", currentModel);
        } catch (Exception e) {
            log.error("Failed to initialize ReAct Agent", e);
        }
    }

    /**
     * 根据配置创建模型实例
     */
    private ChatModelBase createModel() {
        return switch (provider.toLowerCase()) {
            case "ollama" -> {
                currentModel = "Ollama/" + ollamaModelName;
                log.info("Using Ollama model: {} at {}", ollamaModelName, ollamaBaseUrl);
                yield OllamaChatModel.builder()
                        .modelName(ollamaModelName)
                        .baseUrl(ollamaBaseUrl)
                        .build();
            }
            case "openai" -> {
                if (openaiApiKey == null || openaiApiKey.isEmpty() || openaiApiKey.equals("your-api-key-here")) {
                    log.warn("OpenAI API key not configured");
                    yield null;
                }
                currentModel = "OpenAI/" + openaiModelName;
                log.info("Using OpenAI compatible model: {} at {}", openaiModelName, openaiBaseUrl);
                yield OpenAIChatModel.builder()
                        .apiKey(openaiApiKey)
                        .modelName(openaiModelName)
                        .baseUrl(openaiBaseUrl)
                        .build();
            }
            case "anthropic" -> {
                if (anthropicApiKey == null || anthropicApiKey.isEmpty() || anthropicApiKey.equals("your-api-key-here")) {
                    log.warn("Anthropic API key not configured");
                    yield null;
                }
                currentModel = "Anthropic/" + anthropicModelName;
                log.info("Using Anthropic model: {}", anthropicModelName);
                yield AnthropicChatModel.builder()
                        .apiKey(anthropicApiKey)
                        .modelName(anthropicModelName)
                        .build();
            }
            case "gemini" -> {
                if (geminiApiKey == null || geminiApiKey.isEmpty() || geminiApiKey.equals("your-api-key-here")) {
                    log.warn("Gemini API key not configured");
                    yield null;
                }
                currentModel = "Gemini/" + geminiModelName;
                log.info("Using Gemini model: {}", geminiModelName);
                yield GeminiChatModel.builder()
                        .apiKey(geminiApiKey)
                        .modelName(geminiModelName)
                        .build();
            }
            case "dashscope" -> {
                if (dashscopeApiKey == null || dashscopeApiKey.isEmpty() || dashscopeApiKey.equals("your-api-key-here")) {
                    log.warn("DashScope API key not configured");
                    yield null;
                }
                currentModel = "DashScope/" + dashscopeModelName;
                log.info("Using DashScope model: {}", dashscopeModelName);
                yield DashScopeChatModel.builder()
                        .apiKey(dashscopeApiKey)
                        .modelName(dashscopeModelName)
                        .build();
            }
            default -> {
                log.warn("Unknown provider: {}, falling back to Ollama", provider);
                currentModel = "Ollama/" + ollamaModelName;
                yield OllamaChatModel.builder()
                        .modelName(ollamaModelName)
                        .baseUrl(ollamaBaseUrl)
                        .build();
            }
        };
    }

    /**
     * 执行任务
     */
    public String executeTask(String taskName) {
        log.info("Executing task: {}", taskName);

        if (agent == null) {
            return "Agent not initialized. Please configure API key for provider: " + provider;
        }

        try {
            // 创建用户消息
            Msg userMsg = Msg.builder()
                    .textContent(taskName)
                    .build();

            // 调用 Agent
            Msg response = agent.call(userMsg).block();

            if (response != null) {
                String result = response.getTextContent();
                log.info("Task completed: {}", result);
                return result;
            } else {
                return "No response from agent";
            }
        } catch (Exception e) {
            log.error("Error executing task", e);
            return "Error: " + e.getMessage();
        }
    }

    /**
     * 获取 Agent 状态
     */
    public String getStatus() {
        if (agent == null) {
            return AgentStatus.NOT_INITIALIZED;
        }
        return AgentStatus.ACTIVE;
    }

    /**
     * 检查 Agent 是否可用
     */
    public boolean isAvailable() {
        return agent != null;
    }
}
