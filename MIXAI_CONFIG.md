# MixAI OpenAI 兼容 API 配置说明

## 已配置信息

本项目已配置使用 **MixAI** 的 OpenAI 兼容 API。

### 环境变量文件 (`.env`)

项目根目录已创建 `.env` 文件，包含以下配置：

```bash
# OpenAI 兼容 API 配置 (MixAI)
OPENAI_API_KEY=sk-Mw59bm7BCjyKMtZgyDPIzUrK98bOBX8tBoiot0TCQof7f1sq
OPENAI_BASE_URL=https://us.mixaicloud.com/v1

# 模型提供商选择
AGENTSCOPE_PROVIDER=openai

# 模型名称
AGENTSCOPE_MODEL_NAME=qwen-plus
```

## 配置位置

### application.yml
```yaml
agentscope:
  enabled: true
  provider: ${AGENTSCOPE_PROVIDER:openai}
  openai:
    api-key: ${OPENAI_API_KEY:your-api-key-here}
    base-url: ${OPENAI_BASE_URL:https://api.openai.com/v1}
    model-name: ${AGENTSCOPE_MODEL_NAME:qwen-plus}
```

## IDEA 运行配置

已在 `.idea/runConfigurations/AgentServerApplication.xml` 中配置环境变量文件路径：
- 环境变量文件：`$PROJECT_DIR$/.env`

## 使用方式

### 方式 1：IDEA 直接运行
1. 打开 IDEA
2. 找到 `AgentServerApplication` 运行配置
3. 点击运行即可（会自动加载 `.env` 文件）

### 方式 2：Maven 命令运行
```bash
cd D:\Code\java_code\distributed-agentscope
mvn spring-boot:run -pl agent-server
```

### 方式 3：直接运行 JAR
```bash
# Windows PowerShell
$env:OPENAI_API_KEY="sk-Mw59bm7BCjyKMtZgyDPIzUrK98bOBX8tBoiot0TCQof7f1sq"
$env:OPENAI_BASE_URL="https://us.mixaicloud.com/v1"
$env:AGENTSCOPE_PROVIDER="openai"
java -jar agent-server/target/agent-server-1.0.0-SNAPSHOT.jar
```

## 测试 API

### 1. 健康检查
```bash
curl http://localhost:8888/api/health
```

### 2. Agent 状态
```bash
curl http://localhost:8888/api/agent/status
```

### 3. 执行任务
```bash
curl -X POST http://localhost:8888/api/agent/execute \
  -H "Content-Type: application/json" \
  -d '{"taskName": "北京今天天气怎么样？"}'
```

## 可用工具

Agent 已集成以下工具：
- **时间工具**: 获取当前时间、日期，计算时间差
- **计算器工具**: 数学计算、比较数字、百分比
- **天气工具**: 查询天气、天气预报（模拟数据）

## 注意事项

1. **`.env` 文件安全**: `.env` 文件已添加到 `.gitignore`，不会被提交到 Git
2. **API Key 保密**: 请勿将真实的 API Key 提交到版本控制系统
3. **环境变量优先级**: 系统环境变量 > `.env` 文件 > application.yml 默认值

## 故障排查

### 检查 Agent 是否初始化
查看启动日志中是否有：
```
Using OpenAI compatible model: qwen-plus at https://us.mixaicloud.com/v1
ReAct Agent initialized successfully with model: Ollama/qwen-plus
```

### 检查 API 调用
查看日志中是否有：
```
Registered tools: [get_weather, get_current_time, calculate, ...]
```

### 常见错误
1. **401 Unauthorized**: API Key 无效或过期
2. **404 Not Found**: Base URL 配置错误
3. **Connection timeout**: 网络连接问题
