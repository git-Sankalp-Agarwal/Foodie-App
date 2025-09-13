package com.foodorderservice.Foodie.dtos;


import com.foodorderservice.Foodie.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusResponseDTO {
    private Long orderId;
    private OrderStatus status;
    private LocalDateTime orderTime;
    private LocalDateTime processedTime;
}
