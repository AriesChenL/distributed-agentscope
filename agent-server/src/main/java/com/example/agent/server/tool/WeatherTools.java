package com.example.agent.server.tool;

import io.agentscope.core.tool.Tool;
import io.agentscope.core.tool.ToolParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 天气工具类（模拟实现，实际应该调用天气 API）
 */
@Slf4j
@Component
public class WeatherTools {

    private static final Map<String, String> WEATHER_CACHE = new HashMap<>();
    private static final Random RANDOM = new Random();

    static {
        // 初始化一些城市的默认天气
        WEATHER_CACHE.put("beijing", "Sunny, 25°C");
        WEATHER_CACHE.put("shanghai", "Cloudy, 22°C");
        WEATHER_CACHE.put("guangzhou", "Rainy, 28°C");
        WEATHER_CACHE.put("shenzhen", "Partly Cloudy, 27°C");
        WEATHER_CACHE.put("hangzhou", "Sunny, 24°C");
    }

    @Tool(name = "get_weather", description = "Get current weather for a specified city")
    public String getWeather(
            @ToolParam(name = "city", description = "City name, e.g., 'Beijing', 'Shanghai'") String city) {
        log.info("Getting weather for city: {}", city);
        
        String cityLower = city.toLowerCase();
        
        // 模拟 API 调用
        if (WEATHER_CACHE.containsKey(cityLower)) {
            return "Weather in " + city + ": " + WEATHER_CACHE.get(cityLower);
        }
        
        // 随机生成天气（模拟）
        String[] conditions = {"Sunny", "Cloudy", "Rainy", "Partly Cloudy", "Windy"};
        String condition = conditions[RANDOM.nextInt(conditions.length)];
        int temperature = 15 + RANDOM.nextInt(20);
        
        String weather = String.format("%s, %d°C", condition, temperature);
        WEATHER_CACHE.put(cityLower, weather);
        
        return "Weather in " + city + ": " + weather;
    }

    @Tool(name = "get_weather_forecast", description = "Get weather forecast for the next few days")
    public String getWeatherForecast(
            @ToolParam(name = "city", description = "City name") String city,
            @ToolParam(name = "days", description = "Number of days to forecast (1-7)") Integer days) {
        log.info("Getting weather forecast for {} days in {}", days, city);
        
        if (days < 1 || days > 7) {
            return "Error: Days must be between 1 and 7";
        }
        
        StringBuilder forecast = new StringBuilder();
        forecast.append("Weather forecast for ").append(city).append(":\n");
        
        String[] conditions = {"Sunny", "Cloudy", "Rainy", "Partly Cloudy"};
        for (int i = 0; i < days; i++) {
            String condition = conditions[RANDOM.nextInt(conditions.length)];
            int temp = 18 + RANDOM.nextInt(15);
            forecast.append(String.format("  Day %d: %s, %d°C\n", i + 1, condition, temp));
        }
        
        return forecast.toString().trim();
    }
}
