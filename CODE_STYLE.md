# Distributed AgentScope 代码规范

本规范适用于 `distributed-agentscope` 项目的所有模块，所有开发人员必须遵守。

## 目录

1. [项目结构规范](#1-项目结构规范)
2. [命名规范](#2-命名规范)
3. [代码格式规范](#3-代码格式规范)
4. [注释规范](#4-注释规范)
5. [编程规范](#5-编程规范)
6. [异常处理规范](#6-异常处理规范)
7. [日志规范](#7-日志规范)
8. [测试规范](#8-测试规范)
9. [Git 提交规范](#9-git-提交规范)

---

## 1. 项目结构规范

### 1.1 模块划分

```
distributed-agentscope/
├── agent-common/          # 公共模块：DTO、工具类、常量等
├── agent-server/          # 服务端模块：核心业务逻辑
└── agent-client/          # 客户端模块：代理调用
```

### 1.2 包结构规范

每个模块遵循以下包结构：

```
com.example.agent.{module}/
├── controller/      # REST 控制器
├── service/         # 服务层（接口 + 实现）
├── entity/          # 实体类（JPA/MyBatis）
├── document/        # MongoDB 文档类
├── dto/             # 数据传输对象
├── repository/      # 数据访问层
├── mapper/          # MyBatis Mapper
├── config/          # 配置类
├── exception/       # 异常处理
└── util/            # 工具类
```

### 1.3 分层依赖规则

- **controller** → **service** → **repository/mapper**
- 禁止跨层调用（如 controller 直接调用 repository）
- 禁止循环依赖

---

## 2. 命名规范

### 2.1 类命名

| 类型 | 命名规则 | 示例 |
|------|----------|------|
| 类/接口 | 大驼峰，名词或名词短语 | `AgentService`, `User` |
| 实现类 | 接口名 + `Impl` | `AgentServiceImpl` |
| 控制器 | 功能名 + `Controller` | `AgentController` |
| 服务接口 | 功能名 + `Service` | `AgentService` |
| 服务实现 | 功能名 + `ServiceImpl` | `AgentServiceImpl` |
| 仓库接口 | 实体名 + `Repository`/`Mapper` | `AgentRepository`, `AgentMapper` |
| DTO | 功能名 + `DTO`/`Request`/`Response` | `AgentRequest`, `AgentResponse` |
| 实体类 | 表名单数，大驼峰 | `Agent`, `Message` |
| 文档类 | 集合名 + `Document` | `MessageDocument` |
| 配置类 | 功能名 + `Config` | `WebClientConfig` |
| 异常类 | 异常名 + `Exception` | `AgentException` |
| 枚举 | 大驼峰，名词 | `AgentStatus`, `Role` |
| 工具类 | 功能名 + `Util`/`Utils` | `DateUtil` |
| 常量类 | 功能名 + `Constants` | `ApiConstants` |

### 2.2 方法和变量命名

| 类型 | 命名规则 | 示例 |
|------|----------|------|
| 方法 | 小驼峰，动词开头 | `getStatus()`, `executeTask()` |
| 变量 | 小驼峰，名词 | `agentName`, `taskCount` |
| 常量 | 全大写，下划线分隔 | `DEFAULT_TIMEOUT`, `MAX_RETRY` |
| Boolean 变量 | 避免 is 前缀 | `success`, `active` (非 `isSuccess`) |
| 集合变量 | 使用复数形式 | `agents`, `messageList` |
| ID 相关 | 统一使用 `id` | `agentId`, `userId` |

### 2.3 包命名

- 全部小写，单数形式
- 格式：`com.example.agent.{module}.{layer}`

```java
// ✅ 正确
package com.example.agent.server.controller;
package com.example.agent.common.dto;

// ❌ 错误
package com.example.agent.server.controllers;
package com.example.agent.Common.DTO;
```

---

## 3. 代码格式规范

### 3.1 缩进和空格

- 使用 **4 个空格** 缩进（禁止 Tab）
- 操作符两侧各一个空格
- 逗号后一个空格
- 左大括号前一个空格

```java
// ✅ 正确
public class AgentService {
    private String name;
    
    public void doSomething() {
        if (condition) {
            // ...
        }
    }
}

// ❌ 错误
public class AgentService{
  private String name;
  
  public void doSomething(){
    if(condition){
      //...
    }
  }
}
```

### 3.2 空行规则

- 类声明后空一行
- 方法之间空一行
- 方法内逻辑块之间可空一行
- 连续空行不超过 2 行

```java
public class AgentService {
    
    private final AgentRepository repository;
    
    // ✅ 正确：方法间空一行
    public void method1() {
        // ...
    }
    
    public void method2() {
        // ...
    }
}
```

### 3.3 大括号规则

- 使用 K&R 风格（大括号不换行）
- 空的大括号体也需要大括号

```java
// ✅ 正确
if (condition) {
    // ...
}

for (int i = 0; i < 10; i++) {
    // ...
}

// ❌ 错误
if (condition)
    doSomething();
```

### 3.4 行宽

- 单行代码不超过 **120 字符**
- 超长字符串使用 `+` 或 `text block` 换行
- 超长方法调用链换行缩进

```java
// ✅ 正确
String sql = "SELECT * FROM agent " +
             "WHERE status = 'ACTIVE' " +
             "ORDER BY create_time DESC";

result = service.method1(param1, param2)
                  .method2(param3)
                  .method3();
```

### 3.5 Import 规则

- 禁止使用 `*` 通配符导入
- 导入顺序：标准库 → 第三方库 → 项目内部
- 每组之间空一行

```java
// ✅ 正确
import java.util.List;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.agent.common.dto.ApiResponse;

// ❌ 错误
import java.util.*;
import lombok.*;
```

---

## 4. 注释规范

### 4.1 类注释

- 类/接口必须有一行简短描述
- 复杂类需要详细说明用途

```java
/**
 * Agent 服务接口
 */
public interface AgentService {
}

/**
 * Agent 服务实现类
 * <p>
 * 负责处理 Agent 的核心业务逻辑，包括：
 * <ul>
 *     <li>Agent 状态管理</li>
 *     <li>任务执行调度</li>
 * </ul>
 */
@Service
public class AgentServiceImpl implements AgentService {
}
```

### 4.2 方法注释

- 公共方法必须有注释
- 说明方法功能、参数、返回值、异常

```java
/**
 * 根据 ID 获取 Agent 信息
 *
 * @param id Agent 的唯一标识
 * @return Agent 信息，不存在时返回 null
 * @throws AgentNotFoundException 当 Agent 不存在时抛出
 */
public Agent getAgentById(Long id) {
    // ...
}
```

### 4.3 行内注释

- 使用 `//` 单行注释
- 注释与代码之间空一格
- 避免解释"做什么"，重点解释"为什么"

```java
// ✅ 正确
// 使用 ASSIGN_ID 保证分布式环境下 ID 唯一
@TableId(type = IdType.ASSIGN_ID)
private Long id;

// ❌ 错误
// 设置名称
agent.setName(name);
```

### 4.4 TODO 注释

- 使用标准格式：`// TODO: [责任人] 描述`
- 定期清理 TODO

```java
// TODO: AriesChenL - 优化缓存策略，2024-01-15
```

---

## 5. 编程规范

### 5.1 Lombok 使用规范

- 优先使用 `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`
- 服务类使用 `@RequiredArgsConstructor` 注入依赖
- 避免在实体类使用 `@Data`，推荐显式声明

```java
// ✅ 正确：DTO 类
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AgentRequest {
    private String name;
    private String type;
}

// ✅ 正确：服务类
@Slf4j
@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {
    private final AgentRepository repository;
}
```

### 5.2 集合处理规范

- 使用 `Collections.emptyList()` 而非 `null`
- 使用 Stream API 简化集合操作
- 明确指定集合初始容量（已知大小时）

```java
// ✅ 正确
public List<Agent> getAgents() {
    return repository.findAll() != null ? repository.findAll() : Collections.emptyList();
}

// ❌ 错误
public List<Agent> getAgents() {
    return null; // 调用方需要额外判空
}
```

### 5.3 字符串处理规范

- 使用 `String.format()` 或 `text block` 格式化
- 字符串比较使用 `Objects.equals()`
- 多行字符串使用 Text Block（Java 17+）

```java
// ✅ 正确
String message = String.format("Agent %s executed task: %s", agentName, taskName);

String sql = """
    SELECT * FROM agent 
    WHERE status = 'ACTIVE'
    """;

boolean equals = Objects.equals(str1, str2);

// ❌ 错误
String message = "Agent " + agentName + " executed task: " + taskName;
boolean equals = str1.equals(str2); // 可能 NPE
```

### 5.4 Optional 使用规范

- 返回值可能为空时使用 `Optional<T>`
- 使用 `orElse()`, `orElseGet()`, `ifPresent()` 处理

```java
// ✅ 正确
public Optional<Agent> findById(Long id) {
    return repository.findById(id);
}

agentService.findById(id)
    .ifPresent(agent -> log.info("Found agent: {}", agent.getName()));

// ❌ 错误
public Agent findById(Long id) {
    return repository.findById(id).orElse(null);
}
```

### 5.5 时间处理规范

- 使用 `java.time` 包（`LocalDateTime`, `ZonedDateTime`）
- 禁止使用 `Date` 和 `Calendar`
- 数据库时间统一使用 `LocalDateTime`

```java
// ✅ 正确
private LocalDateTime createTime;
LocalDateTime now = LocalDateTime.now();

// ❌ 错误
private Date createTime;
Date now = new Date();
```

---

## 6. 异常处理规范

### 6.1 异常分类

- **业务异常**：使用自定义异常，如 `AgentException`
- **系统异常**：抛出 `RuntimeException`
- **检查异常**：转换为非检查异常或明确声明

### 6.2 全局异常处理

- 使用 `@RestControllerAdvice` 统一处理
- 返回统一的错误响应格式

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(AgentException.class)
    public ApiResponse<Void> handleAgentException(AgentException e) {
        return ApiResponse.error(e.getCode(), e.getMessage());
    }
    
    @ExceptionHandler(Exception.class)
    public ApiResponse<Void> handleException(Exception e) {
        log.error("系统异常", e);
        return ApiResponse.error(500, "系统内部错误");
    }
}
```

### 6.3 异常抛出规范

- 禁止捕获异常后不处理
- 禁止吞掉异常（空的 catch 块）
- 异常信息必须包含关键上下文

```java
// ✅ 正确
try {
    agentRepository.findById(id);
} catch (Exception e) {
    log.error("获取 Agent 失败，id: {}", id, e);
    throw new AgentException("获取 Agent 失败：" + id, e);
}

// ❌ 错误
try {
    // ...
} catch (Exception e) {
    // 空 catch，吞掉异常
}
```

---

## 7. 日志规范

### 7.1 日志级别使用

| 级别 | 使用场景 |
|------|----------|
| ERROR | 系统错误、异常堆栈 |
| WARN | 警告信息、可恢复错误 |
| INFO | 关键业务流程、状态变更 |
| DEBUG | 调试信息、详细参数 |
| TRACE | 最详细的追踪信息 |

### 7.2 日志格式

- 使用 SLF4J 占位符
- 日志信息包含关键上下文
- 异常必须记录堆栈

```java
@Slf4j
@Service
public class AgentServiceImpl {
    
    // ✅ 正确
    public void executeTask(String taskName) {
        log.info("开始执行任务：{}", taskName);
        log.debug("任务参数：{}", taskName);
    }
    
    public void handleError(Exception e, Long agentId) {
        log.error("任务执行失败，agentId: {}", agentId, e);
    }
    
    // ❌ 错误
    log.info("开始执行任务：" + taskName); // 字符串拼接
    log.error("任务执行失败"); // 缺少上下文和堆栈
}
```

### 7.3 日志内容规范

- 禁止打印敏感信息（密码、Token 等）
- 生产环境避免打印大量 DEBUG 日志
- 异常日志必须包含完整堆栈

---

## 8. 测试规范

### 8.1 测试类命名

- 测试类名：`被测试类名 + Test`
- 测试方法：`test + 方法名 + 场景`

```java
// ✅ 正确
class AgentServiceTest {
    @Test
    void testGetStatus() { }
    
    @Test
    void testExecuteTask_withValidName_returnsSuccess() { }
}
```

### 8.2 测试结构

- 使用 AAA 模式：Arrange - Act - Assert
- 每个测试方法只验证一个场景

```java
@Test
void testExecuteTask() {
    // Arrange
    String taskName = "test-task";
    
    // Act
    String result = agentService.executeTask(taskName);
    
    // Assert
    assertTrue(result.contains(taskName));
    assertTrue(result.contains("executed successfully"));
}
```

### 8.3 测试覆盖要求

- 核心业务逻辑覆盖率 ≥ 80%
- 公共方法必须有单元测试
- Controller 层可写集成测试

### 8.4 断言规范

- 使用 JUnit 5 断言
- 断言信息清晰明确

```java
// ✅ 正确
assertEquals(expected, actual, "状态应该匹配");
assertTrue(result.contains("success"), "结果应包含 success");

// ❌ 错误
if (!result.contains("success")) {
    throw new AssertionError();
}
```

---

## 9. Git 提交规范

### 9.1 提交信息格式

```
<type>(<scope>): <subject>

<body>

<footer>
```

### 9.2 Type 类型

| 类型 | 说明 |
|------|------|
| feat | 新功能 |
| fix | Bug 修复 |
| docs | 文档更新 |
| style | 代码格式（不影响功能） |
| refactor | 重构 |
| test | 测试相关 |
| chore | 构建/工具/配置 |

### 9.3 提交示例

```
feat(agent): 添加 Agent 状态查询接口

- 新增 /api/agent/status 接口
- 实现状态查询逻辑

Closes #123

---

fix(server): 修复 Agent 创建时空指针异常

当 endpoint 为空时会导致 NPE，添加空值检查

---

docs(readme): 更新快速开始文档

补充 Docker 启动说明
```

### 9.4 分支管理

- `main`：主分支，生产环境代码
- `develop`：开发分支
- `feature/*`：功能分支（从 develop 切出）
- `fix/*`：修复分支
- `release/*`：发布分支

---

## 附录：IDE 配置建议

### IntelliJ IDEA

1. **代码风格**：导入项目根目录的 `.editorconfig`
2. **自动格式化**：保存时自动格式化（File Watchers）
3. **检查级别**：启用所有 Inspection

### 推荐的 Maven 插件

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-checkstyle-plugin</artifactId>
    <version>3.3.0</version>
</plugin>
```

### 检查命令

```bash
# 代码格式化检查
mvn checkstyle:check

# 编译
mvn clean install

# 测试
mvn clean test
```
