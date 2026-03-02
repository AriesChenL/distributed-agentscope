-- Agent 表
CREATE TABLE IF NOT EXISTS agent (
    id BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT 'Agent 名称',
    type VARCHAR(50) NOT NULL COMMENT 'Agent 类型',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE, INACTIVE, BUSY',
    endpoint VARCHAR(255) COMMENT '端点地址',
    task_count INT DEFAULT 0 COMMENT '任务数量',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted INT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除'
);

-- 插入测试数据
INSERT INTO agent (id, name, type, status, endpoint, task_count) VALUES
(1, 'Agent-001', 'CHAT', 'ACTIVE', 'http://localhost:8081', 0),
(2, 'Agent-002', 'TASK', 'BUSY', 'http://localhost:8082', 5),
(3, 'Agent-003', 'ANALYSIS', 'INACTIVE', 'http://localhost:8083', 0);
