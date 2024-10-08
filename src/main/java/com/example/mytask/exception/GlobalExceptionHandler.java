package com.example.mytask.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // 유효성 검사 관련 예외
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 일반적인 예외
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 비밀번호 관련 예외
    @ExceptionHandler(PasswordRequiredException.class)
    public ResponseEntity<String> handlePasswordRequiredException(PasswordRequiredException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 비밀번호 불일치 예외
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<String> handlePasswordMismatchException(PasswordMismatchException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 중복 키 예외
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<String> handleDuplicateKeyException(DuplicateKeyException ex) {
        if (ex.getMessage().contains("assignee.email")) {
            return new ResponseEntity<>("이미 존재하는 이메일 주소입니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("중복된 값이 있습니다: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 데이터베이스 관련 예외
    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<String> handleDataAccessException(DataAccessException ex) {
        return new ResponseEntity<>("데이터베이스 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 데이터 무결성 위반 예외
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        if (ex.getMessage().contains("task_name")) {
            return new ResponseEntity<>("일정 이름은 최소 3자 이상이어야 합니다.", HttpStatus.BAD_REQUEST);
        }
        if (ex.getMessage().contains("password")) {
            return new ResponseEntity<>("비밀번호는 최소 8자 이상이어야 합니다.", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("데이터 무결성에 문제가 발생했습니다.", HttpStatus.BAD_REQUEST);
    }

    // HTTP 메서드 지원 예외
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        StringBuilder errorMessage = new StringBuilder("잘못된 HTTP 메서드입니다. 사용 가능한 메서드 : ");
        for (HttpMethod method : ex.getSupportedHttpMethods()) {
            errorMessage.append(method.name()).append(" ");
        }
        return new ResponseEntity<>(errorMessage.toString().trim(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    // 리소스 찾기 실패 예외
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 할 일 목록이 없을 때 예외
    @ExceptionHandler(NoTasksFoundException.class)
    public ResponseEntity<String> handleNoTasksFoundException(NoTasksFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

}