package com.foodorderservice.Foodie.advices;


import com.foodorderservice.Foodie.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseWrapper<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.error("Validation failed: {}", errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(ApiResponseWrapper.<Map<String, String>>builder()
                                                     .success(false)
                                                     .message("Validation failed")
                                                     .data(errors)
                                                     .build());
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleOrderNotFoundException(
            OrderNotFoundException ex, WebRequest request) {
        log.error("Order not found: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(ApiResponseWrapper.<Void>builder()
                                                     .success(false)
                                                     .message(ex.getMessage())
                                                     .build());
    }

    @ExceptionHandler(InvalidOrderStateException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleInvalidOrderStateException(
            InvalidOrderStateException ex, WebRequest request) {
        log.error("Invalid order state: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(ApiResponseWrapper.<Void>builder()
                                                     .success(false)
                                                     .message(ex.getMessage())
                                                     .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseWrapper<String>> handleGlobalException(
            Exception ex, WebRequest request) {
        log.error("Unexpected error occurred", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(ApiResponseWrapper.<String>builder()
                                                     .success(false)
                                                     .message("An unexpected error occurred")
                                                     .data(ex.getMessage())
                                                     .build());
    }
}















