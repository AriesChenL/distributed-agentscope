# Distributed AgentScope

分布式 AgentScope 项目，基于 Spring Boot 3.2.0 构建的多模块项目。

## 项目结构

```
distributed-agentscope/
├── pom.xml                          # 父 POM
├── agent-common/                    # 公共模块
│   ├── dto/                         # 数据传输对象
│   └── ...
├── agent-server/                    # 服务端模块 (端口 8080)
│   ├── controller/                  # REST 控制器
│   ├── service/                     # 服务层
│   ├── entity/                      # 实体类
│   ├── mapper/                      # MyBatis Mapper
│   ├── config/                      # 配置类
│   └── exception/                   # 异常处理
└── agent-client/                    # 客户端模块 (端口 8081)
    ├── controller/
    ├── service/
    └── config/
```

## 技术栈

- **Spring Boot** 3.2.0
- **MyBatis Plus** 3.5.5 (关系型数据存储)
- **MongoDB** (文档数据库，用于消息存储)
- **H2 Database** (内存数据库)
- **Lombok**
- **WebFlux WebClient** (HTTP 客户端)
- **JUnit 5** (单元测试)

## 快速开始

### 编译项目
```bash
mvn clean install
```

### 启动服务端
```bash
cd agent-server
mvn spring-boot:run
```

### 启动客户端
```bash
cd agent-client
mvn spring-boot:run
```

## API 接口

### Agent Server (http://localhost:8080)

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/health` | 健康检查 |
| GET | `/api/info` | 服务信息 |
| GET | `/api/agent/status` | Agent 状态 |
| POST | `/api/agent/execute` | 执行任务 |
| GET | `/api/agents` | 获取所有 Agent |
| GET | `/api/agents/{id}` | 获取指定 Agent |
| POST | `/api/agents` | 创建 Agent |
| PUT | `/api/agents/{id}` | 更新 Agent |
| DELETE | `/api/agents/{id}` | 删除 Agent |
| GET | `/api/agents/status/{status}` | 按状态查询 Agent |
| POST | `/api/messages` | 保存消息 |
| GET | `/api/messages/conversation/{conversationId}` | 获取会话消息（分页） |
| GET | `/api/messages/conversation/{conversationId}/all` | 获取会话所有消息 |
| GET | `/api/messages/agent/{agentId}` | 获取 Agent 的消息 |
| GET | `/api/messages/conversation/{conversationId}/range` | 按时间范围查询消息 |
| GET | `/api/messages/conversation/{conversationId}/count` | 获取会话消息数量 |
| DELETE | `/api/messages/conversation/{conversationId}` | 删除会话所有消息 |

### Agent Client (http://localhost:8081)

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/health` | 健康检查 |
| GET | `/api/info` | 服务信息 |
| GET | `/api/proxy/health` | 代理获取服务端健康状态 |
| POST | `/api/proxy/execute` | 代理执行服务端任务 |

## 数据库

### MongoDB (消息存储)

- URI: `mongodb://localhost:27017/agentscope`
- 数据库：`agentscope`
- 集合：`messages`

### H2 (关系型数据存储)

使用 H2 内存数据库，控制台地址：http://localhost:8080/h2-console

- JDBC URL: `jdbc:h2:mem:agentdb`
- 用户名：`sa`
- 密码：(空)

## 测试

```bash
mvn clean test
```

## 示例请求

### 创建 Agent
```bash
curl -X POST http://localhost:8080/api/agents \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test-Agent",
    "type": "CHAT",
    "status": "ACTIVE",
    "endpoint": "http://localhost:8081"
  }'
```

### 执行任务
```bash
curl -X POST http://localhost:8080/api/agent/execute \
  -H "Content-Type: application/json" \
  -d '{"taskName": "my-task"}'
```

### 客户端代理调用
```bash
curl -X POST http://localhost:8081/api/proxy/execute \
  -H "Content-Type: application/json" \
  -d '{"taskName": "my-task"}'
```

### 保存消息
```bash
curl -X POST http://localhost:8080/api/messages \
  -H "Content-Type: application/json" \
  -d '{
    "conversationId": "conv-001",
    "agentId": "1",
    "role": "USER",
    "content": "Hello",
    "metadata": {"source": "web"}
  }'
```

### 获取会话消息
```bash
curl http://localhost:8080/api/messages/conversation/conv-001?page=0&size=20
```
