package com.example.mytask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // 예외를 처리하는 클래스 어노테이션
public class GlobalExceptionHandler {

    // 400 Bad Request (입력값이 잘못됨 )
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>("잘못된 입력입니다. : " + ex.getBindingResult().getFieldError().getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }

    // 401 Unauthorized (비밀번호 불일치 )
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<String> handlePasswordMismatchException(PasswordMismatchException ex) {
        return new ResponseEntity<>("비밀번호가 일치하지 않습니다. : " + ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // 404 Not Found (특정 ID의 일정이 없을 때 )
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>("리소스를 찾을 수 없습니다.: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // 500 Internal Server Error (기타 예상불가 예외 )
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return new ResponseEntity<>("서버 오류가 발생했습니다. : " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
