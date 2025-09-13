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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private <T> ResponseEntity<ApiResponseWrapper<T>> buildErrorResponse(HttpStatus status, String message, List<String> subErrors) {
        ApiError apiError = ApiError.builder()
                                    .status(status)
                                    .message(message)
                                    .subErrors(subErrors)
                                    .build();

        ApiResponseWrapper<T> wrapper = ApiResponseWrapper.<T>builder()
                                                          .success(false)
                                                          .message(message)
                                                          .error(apiError)
                                                          .build();

        return ResponseEntity.status(status).body(wrapper);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = (error instanceof FieldError) ? ((FieldError) error).getField() : error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.add(field + ": " + errorMessage);
        });

        log.warn("Validation failed: {}", errors);
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Validation failed", errors);
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleOrderNotFoundException(OrderNotFoundException ex, WebRequest request) {
        log.warn("Order not found: {}", ex.getMessage());
        List<String> sub = List.of(ex.getMessage());
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Order not found", sub);
    }

    @ExceptionHandler(InvalidOrderStateException.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleInvalidOrderStateException(InvalidOrderStateException ex, WebRequest request) {
        log.warn("Invalid order state: {}", ex.getMessage());
        List<String> sub = List.of(ex.getMessage());
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "Invalid order state", sub);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseWrapper<Void>> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Unexpected error occurred", ex); // full stack trace in logs


        String clientMessage = "An unexpected error occurred";
        List<String> sub = List.of(ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName());

        return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, clientMessage, sub);
    }
}













