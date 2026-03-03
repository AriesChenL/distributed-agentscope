# 代码规范快速参考

## 常用命令

```bash
# 代码格式化
mvn spotless:apply

# 代码规范检查
mvn checkstyle:check

# 编译 + 测试
mvn clean install
```

## 命名速查

| 类型 | 规范 | 示例 |
|------|------|------|
| 类 | 大驼峰 | `AgentService` |
| 方法 | 小驼峰，动词开头 | `getStatus()` |
| 变量 | 小驼峰 | `agentName` |
| 常量 | 全大写，下划线 | `MAX_SIZE` |
| 测试类 | `XxxTest` | `AgentServiceTest` |

## 代码格式

- **缩进**: 4 个空格（禁用 Tab）
- **行宽**: ≤ 120 字符
- **大括号**: K&R 风格（不换行）
- **导入**: 禁止 `*`，分组空行

## 注释要求

- 类：必须有描述
- 公共方法：必须有注释
- 复杂逻辑：必须有解释性注释
- TODO: `// TODO: [姓名] 描述`

## 最佳实践

### ✅ 推荐

```java
// 使用 Optional
public Optional<Agent> findById(Long id) {
    return repository.findById(id);
}

// 使用集合空值
return list != null ? list : Collections.emptyList();

// 使用日志占位符
log.info("Processing task: {}", taskName);

// 使用 try-with-resources
try (InputStream is = file.getInputStream()) {
    // ...
}
```

### ❌ 避免

```java
// 返回 null
public Agent findById(Long id) {
    return repository.findById(id).orElse(null);
}

// 字符串拼接日志
log.info("Processing task: " + taskName);

// 吞掉异常
try {
    // ...
} catch (Exception e) {
    // 空
}
```

## Git 提交

```bash
# 格式：<type>(<scope>): <subject>
git commit -m "feat(agent): 添加 Agent 状态查询接口"
git commit -m "fix(server): 修复空指针异常"
git commit -m "docs(readme): 更新文档"
```

## IDE 快捷键

### IntelliJ IDEA

- 格式化代码：`Ctrl + Alt + L`
- 优化导入：`Ctrl + Alt + O`
- 生成代码：`Alt + Insert`
- 重构：`Shift + F6`

### Eclipse

- 格式化代码：`Ctrl + Shift + F`
- 优化导入：`Ctrl + Shift + O`
- 生成代码：`Alt + Shift + S`
