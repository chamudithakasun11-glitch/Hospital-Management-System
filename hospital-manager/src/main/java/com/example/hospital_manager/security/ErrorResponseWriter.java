package com.example.hospital_manager.security;

import com.example.hospital_manager.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ErrorResponseWriter {
    public static void writeErrorResponse(HttpServletResponse response, int status, String error, String message) throws IOException {
        response.setContentType("application/json");
        response.setStatus(status);
        
        ErrorResponse errorResponse = new ErrorResponse(status, error, message);
        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}