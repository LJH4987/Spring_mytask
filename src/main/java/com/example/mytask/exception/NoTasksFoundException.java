package com.example.mytask.exception;

public class NoTasksFoundException extends RuntimeException {
    public NoTasksFoundException(String message) {
        super(message);
    }
}