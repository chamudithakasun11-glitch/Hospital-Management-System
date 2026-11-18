package com.example.hospital_manager.dto;

public record ErrorResponse(
    int status,
    String error,
    String message
) {}