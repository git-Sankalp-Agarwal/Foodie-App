package com.foodorderservice.Foodie.advices;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponseWrapper<T> {

    private final boolean success;

    private final String message;

    @Builder.Default
    private final LocalDateTime timeStamp = LocalDateTime.now();

    private final T data;

    private final ApiError error;
}

