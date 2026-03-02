package com.example.agent.server.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Agent 实体类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("agent")
public class Agent {
    
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private String name;
    
    private String type;
    
    private String status;
    
    private String endpoint;
    
    private Integer taskCount;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    private Integer deleted;
}
