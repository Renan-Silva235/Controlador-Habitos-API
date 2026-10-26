package com.habito_controle.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
  public record ErrorResponse(int status, String message, LocalDateTime timestamp){}
  public record ValidationErrorResponse(int status, List<String> errors, LocalDateTime timestamp) {}

  private ResponseEntity<ErrorResponse> build(HttpStatus status, String message){
    return ResponseEntity.status(status).body(new ErrorResponse(status.value(), message, LocalDateTime.now()));
  }

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ErrorResponse> handleApiException(ApiException error){
    return build(error.getStatus(), error.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ValidationErrorResponse> handleValidation(MethodArgumentNotValidException error){
    List<String> messages = error.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(fieldError -> fieldError.getDefaultMessage())
            .toList();

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ValidationErrorResponse(HttpStatus.BAD_REQUEST.value(), messages, LocalDateTime.now()));
  }
}