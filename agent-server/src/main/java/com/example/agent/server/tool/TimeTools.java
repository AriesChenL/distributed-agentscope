package com.example.agent.server.tool;

import io.agentscope.core.tool.Tool;
import io.agentscope.core.tool.ToolParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * 时间工具类
 */
@Slf4j
@Component
public class TimeTools {

    @Tool(name = "get_current_time", description = "Get the current time in a specified timezone")
    public String getCurrentTime(
            @ToolParam(name = "timezone", description = "Timezone name, e.g., 'Beijing', 'UTC', 'America/New_York'") String timezone) {
        log.info("Getting current time for timezone: {}", timezone);
        
        // 简单实现，实际应该根据时区获取
        String time = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return "Current time: " + time + " (timezone: " + timezone + ")";
    }

    @Tool(name = "get_current_date", description = "Get the current date")
    public String getCurrentDate() {
        log.info("Getting current date");
        
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return "Current date: " + date;
    }

    @Tool(name = "calculate_time_difference", description = "Calculate time difference between two times")
    public String calculateTimeDifference(
            @ToolParam(name = "time1", description = "First time in HH:mm:ss format") String time1,
            @ToolParam(name = "time2", description = "Second time in HH:mm:ss format") String time2) {
        log.info("Calculating time difference between {} and {}", time1, time2);
        
        try {
            LocalTime t1 = LocalTime.parse(time1, DateTimeFormatter.ofPattern("HH:mm:ss"));
            LocalTime t2 = LocalTime.parse(time2, DateTimeFormatter.ofPattern("HH:mm:ss"));
            
            long seconds = Math.abs(t1.toSecondOfDay() - t2.toSecondOfDay());
            long hours = seconds / 3600;
            long minutes = (seconds % 3600) / 60;
            long secs = seconds % 60;
            
            return String.format("Time difference: %d hours, %d minutes, %d seconds", hours, minutes, secs);
        } catch (Exception e) {
            log.error("Error calculating time difference", e);
            return "Error: Invalid time format. Please use HH:mm:ss";
        }
    }
}
