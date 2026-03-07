# AgentScope 集成说明

## 概述

本项目已集成 **AgentScope Java**，这是一个用于构建 LLM 驱动的智能代理的框架。

## 支持的模型提供商

| 提供商 | 配置项 | 模型示例 | 费用 |
|--------|--------|----------|------|
| **Ollama** (推荐) | `ollama` | llama3.2, qwen3-max, mistral, phi3 | 免费 (本地) |
| **DeepSeek** | `openai` | deepseek-chat, deepseek-coder | 便宜 |
| **OpenAI** | `openai` | gpt-4o, gpt-4o-mini | 贵 |
| **Anthropic** | `anthropic` | claude-sonnet-4-5, claude-3-opus | 贵 |
| **Gemini** | `gemini` | gemini-2.5-flash, gemini-2.0-flash | 免费额度 |
| **DashScope** | `dashscope` | qwen3-max, qwen-vl-max | 便宜 |

## 快速配置

### 方式 1：使用 Ollama (免费本地模型)

1. **安装 Ollama**: https://ollama.ai

2. **下载模型**:
   ```bash
   ollama pull qwen3-max
   # 或者
   ollama pull llama3.2
   ```

3. **配置 application.yml**:
   ```yaml
   agentscope:
     provider: ollama
     ollama:
       base-url: http://localhost:11434
       model-name: qwen3-max
   ```

### 方式 2：使用 DeepSeek (便宜，中文能力强)

1. **获取 API Key**: https://platform.deepseek.com/

2. **配置 application.yml**:
   ```yaml
   agentscope:
     provider: openai
     openai:
       api-key: sk-xxxxxxxxxxxxxxxx  # DeepSeek API Key
       base-url: https://api.deepseek.com
       model-name: deepseek-chat
   ```

### 方式 3：使用其他提供商

修改 `application.yml`:
```yaml
agentscope:
  provider: anthropic  # 或 gemini, dashscope
  anthropic:
    api-key: sk-ant-xxxx
    model-name: claude-sonnet-4-5-20250929
```

## 已实现功能

### 1. 工具类（Tools）

已创建以下工具类，使用 `@Tool` 注解定义：

#### TimeTools - 时间工具
- `get_current_time` - 获取指定时区的当前时间
- `get_current_date` - 获取当前日期
- `calculate_time_difference` - 计算两个时间之间的差值

#### CalculatorTools - 计算器工具
- `calculate` - 执行基本算术运算（加减乘除）
- `compare_numbers` - 比较两个数字
- `percentage` - 计算百分比

#### WeatherTools - 天气工具（模拟）
- `get_weather` - 获取指定城市的天气
- `get_weather_forecast` - 获取未来几天的天气预报

### 2. RealAgentService - 真正的 Agent 服务

使用 AgentScope 的 `ReActAgent` 实现，支持：
- ReAct 推理循环（Reasoning + Acting）
- 工具自动调用
- 多轮对话记忆

### 3. API 接口

| 接口 | 描述 |
|------|------|
| `GET /api/agent/status` | 获取 Agent 状态（会显示是 REAL_AGENT 还是 SIMULATED） |
| `POST /api/agent/execute` | 执行任务（自动切换到真正的 Agent） |

## 配置方法

### 1. 获取 DashScope API Key

1. 访问 [阿里云 DashScope](https://dashscope.console.aliyun.com/)
2. 注册/登录账号
3. 创建 API Key

### 2. 配置 API Key

**方式一：修改 application.yml**
```yaml
agentscope:
  enabled: true
  dashscope:
    api-key: sk-xxxxxxxxxxxxxxxx  # 替换为您的 API Key
```

**方式二：使用环境变量**
```bash
# Windows PowerShell
$env:DASHSCOPE_API_KEY="sk-xxxxxxxxxxxxxxxx"

# Windows CMD
set DASHSCOPE_API_KEY=sk-xxxxxxxxxxxxxxxx

# Linux/Mac
export DASHSCOPE_API_KEY=sk-xxxxxxxxxxxxxxxx
```

### 3. 启动服务

重启 `AgentServerApplication` 即可。

## 使用示例

### 示例 1：查询天气

```bash
curl -X POST http://localhost:8888/api/agent/execute \
  -H "Content-Type: application/json" \
  -d '{"taskName": "北京今天天气怎么样？"}'
```

### 示例 2：数学计算

```bash
curl -X POST http://localhost:8888/api/agent/execute \
  -H "Content-Type: application/json" \
  -d '{"taskName": "计算 125 + 378 * 2"}'
```

### 示例 3：查询时间

```bash
curl -X POST http://localhost:8888/api/agent/execute \
  -H "Content-Type: application/json" \
  -d '{"taskName": "现在几点了？"}'
```

### 示例 4：复杂任务

```bash
curl -X POST http://localhost:8888/api/agent/execute \
  -H "Content-Type: application/json" \
  -d '{"taskName": "北京和上海的温度差是多少？北京 25 度，上海 28 度"}'
```

## 添加新工具

### 步骤 1：创建工具类

```java
package com.example.agent.server.tool;

import io.agentscope.core.tool.Tool;
import io.agentscope.core.tool.ToolParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MyTools {

    @Tool(name = "my_tool", description = "我的工具描述")
    public String myMethod(
            @ToolParam(name = "param1", description = "参数 1 描述") String param1,
            @ToolParam(name = "param2", description = "参数 2 描述") Integer param2) {
        // 实现逻辑
        return "结果";
    }
}
```

### 步骤 2：注册到 RealAgentService

```java
private final MyTools myTools;

@PostConstruct
public void init() {
    Toolkit toolkit = new Toolkit();
    toolkit.registerTool(timeTools);
    toolkit.registerTool(calculatorTools);
    toolkit.registerTool(weatherTools);
    toolkit.registerTool(myTools);  // 添加新工具
    // ...
}
```

## MCP 集成（Model Context Protocol）

### 添加 MCP 依赖

在 `agent-server/pom.xml` 中添加：

```xml
<!-- MCP StdIO 传输 -->
<dependency>
    <groupId>io.agentscope</groupId>
    <artifactId>agentscope-mcp</artifactId>
    <version>1.0.9</version>
</dependency>
```

### 配置 MCP 客户端

```java
import io.agentscope.core.tool.mcp.McpClientBuilder;
import io.agentscope.core.tool.mcp.McpClientWrapper;

@PostConstruct
public void initMcp() {
    // 连接本地 MCP 服务器（如文件系统）
    McpClientWrapper mcpClient = McpClientBuilder.create("filesystem-mcp")
        .stdioTransport("npx", "-y", "@modelcontextprotocol/server-filesystem", "/tmp")
        .buildAsync()
        .block();
    
    // 注册到 Toolkit
    toolkit.registerMcpClient(mcpClient).block();
}
```

### 可用的 MCP 服务器

| 服务器 | 命令 | 描述 |
|--------|------|------|
| 文件系统 | `npx -y @modelcontextprotocol/server-filesystem /path` | 文件读写 |
| Git | `python -m mcp_server_git` | Git 操作 |
| 数据库 | 自定义 | 数据库查询 |

## 故障排查

### 1. Agent 未初始化

检查日志中是否有：
```
DashScope API key not configured. Agent will not work properly.
```

解决方法：配置 API Key

### 2. 工具未注册

检查日志中是否有：
```
Registered tool 'xxx' in group 'ungrouped'
```

确认所有工具都已注册。

### 3. 调用超时

调整超时配置：
```yaml
agentscope:
  model-timeout: 120s  # 模型调用超时
  tool-timeout: 60s    # 工具调用超时
```

## 参考资料

- [AgentScope Java 文档](https://java.agentscope.io/)
- [AgentScope GitHub](https://github.com/agentscope-ai/agentscope-java)
- [DashScope API 文档](https://help.aliyun.com/zh/dashscope/)
- [MCP 协议规范](https://modelcontextprotocol.io/)
