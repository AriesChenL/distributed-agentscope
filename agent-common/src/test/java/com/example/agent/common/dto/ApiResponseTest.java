package com.example.agent.common.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ApiResponse 测试
 */
class ApiResponseTest {
    
    @Test
    void testSuccess() {
        String data = "test data";
        ApiResponse<String> response = ApiResponse.success(data);
        
        assertEquals(200, response.getCode());
        assertEquals("success", response.getMessage());
        assertEquals(data, response.getData());
    }
    
    @Test
    void testError() {
        ApiResponse<Void> response = ApiResponse.error(400, "Bad Request");
        
        assertEquals(400, response.getCode());
        assertEquals("Bad Request", response.getMessage());
        assertNull(response.getData());
    }
    
    @Test
    void testErrorWithMessage() {
        ApiResponse<Void> response = ApiResponse.error("Server Error");
        
        assertEquals(500, response.getCode());
        assertEquals("Server Error", response.getMessage());
    }
}
