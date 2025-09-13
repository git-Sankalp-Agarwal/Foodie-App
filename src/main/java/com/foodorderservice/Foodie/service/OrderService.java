package com.foodorderservice.Foodie.service;

import com.foodorderservice.Foodie.dtos.OrderRequestDTO;
import com.foodorderservice.Foodie.dtos.OrderResponseDTO;
import com.foodorderservice.Foodie.dtos.OrderStatusUpdateDTO;
import com.foodorderservice.Foodie.dtos.PageResponseDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDTO updateOrderStatus(Long id, @Valid OrderStatusUpdateDTO statusUpdate);

    OrderResponseDTO getOrderById(Long id);

    PageResponseDTO<OrderResponseDTO> getAllOrders(Pageable pageable);

    OrderResponseDTO createOrder(@Valid OrderRequestDTO orderRequest);
}
