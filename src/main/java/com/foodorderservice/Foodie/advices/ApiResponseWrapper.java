package com.foodorderservice.Foodie.advices;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseWrapper<T> {

    private boolean success;
    private String message;
    private LocalDateTime timeStamp = LocalDateTime.now();
    private T data;
    private ApiError error;

    public ApiResponseWrapper(T data) {
        this();
        this.data = data;
    }

    public ApiResponseWrapper(ApiError error) {
        this();
        this.error = error;
    }
}
