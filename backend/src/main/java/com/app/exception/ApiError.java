package com.app.exception;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiError {

    private String message;
    private int status;
    private LocalDateTime timestamp;

    public ApiError(String message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

}