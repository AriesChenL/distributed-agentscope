package com.example.agent.server.tool;

import io.agentscope.core.tool.Tool;
import io.agentscope.core.tool.ToolParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 计算器工具类
 */
@Slf4j
@Component
public class CalculatorTools {

    @Tool(name = "calculate", description = "Perform basic arithmetic calculations")
    public String calculate(
            @ToolParam(name = "expression", description = "Mathematical expression, e.g., '2 + 3 * 4'") String expression) {
        log.info("Calculating expression: {}", expression);
        
        try {
            // 简单的表达式计算，生产环境应该使用更安全的解析器
            Object result = evaluateExpression(expression);
            return "Result: " + result;
        } catch (Exception e) {
            log.error("Error calculating expression", e);
            return "Error: Invalid expression - " + e.getMessage();
        }
    }

    @Tool(name = "compare_numbers", description = "Compare two numbers")
    public String compareNumbers(
            @ToolParam(name = "num1", description = "First number") Double num1,
            @ToolParam(name = "num2", description = "Second number") Double num2) {
        log.info("Comparing {} and {}", num1, num2);
        
        if (num1 > num2) {
            return String.format("%.2f is greater than %.2f", num1, num2);
        } else if (num1 < num2) {
            return String.format("%.2f is less than %.2f", num1, num2);
        } else {
            return String.format("%.2f is equal to %.2f", num1, num2);
        }
    }

    @Tool(name = "percentage", description = "Calculate percentage")
    public String percentage(
            @ToolParam(name = "value", description = "The value") Double value,
            @ToolParam(name = "total", description = "The total value") Double total) {
        log.info("Calculating percentage of {} / {}", value, total);
        
        if (total == 0) {
            return "Error: Division by zero";
        }
        
        double result = (value / total) * 100;
        return String.format("%.2f is %.2f%% of %.2f", value, result, total);
    }

    private Object evaluateExpression(String expression) {
        // 简单的表达式求值，仅支持基本运算
        // 生产环境应该使用专门的表达式解析库
        if (expression.contains("+")) {
            String[] parts = expression.split("\\+");
            return Double.parseDouble(parts[0].trim()) + Double.parseDouble(parts[1].trim());
        } else if (expression.contains("-")) {
            String[] parts = expression.split("-", 2);
            return Double.parseDouble(parts[0].trim()) - Double.parseDouble(parts[1].trim());
        } else if (expression.contains("*")) {
            String[] parts = expression.split("\\*");
            return Double.parseDouble(parts[0].trim()) * Double.parseDouble(parts[1].trim());
        } else if (expression.contains("/")) {
            String[] parts = expression.split("/");
            return Double.parseDouble(parts[0].trim()) / Double.parseDouble(parts[1].trim());
        } else {
            return Double.parseDouble(expression);
        }
    }
}
