-- 插入测试数据
INSERT INTO agent (id, name, type, status, endpoint, task_count) VALUES
(1, 'Agent-001', 'CHAT', 'ACTIVE', 'http://localhost:8081', 0),
(2, 'Agent-002', 'TASK', 'BUSY', 'http://localhost:8082', 5),
(3, 'Agent-003', 'ANALYSIS', 'INACTIVE', 'http://localhost:8083', 0);
