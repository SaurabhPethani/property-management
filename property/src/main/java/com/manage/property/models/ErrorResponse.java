package com.manage.property.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private LocalDateTime timestamp;
    private String error;
    private String message;
    private String details;

    public ErrorResponse(LocalDateTime timestamp, String error, String message, String details) {
        this.timestamp = timestamp;
        this.error = error;
        this.message = message;
        this.details = details;
    }
}
