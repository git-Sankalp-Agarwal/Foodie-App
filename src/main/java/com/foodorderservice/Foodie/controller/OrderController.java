package com.foodorderservice.Foodie.controller;

import com.foodorderservice.Foodie.advices.ApiResponseWrapper;
import com.foodorderservice.Foodie.dtos.*;
import com.foodorderservice.Foodie.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "Order Management", description = "APIs for managing food delivery orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @Operation(summary = "Create a new order", description = "Creates a new food delivery order")
    public ResponseEntity<ApiResponseWrapper<OrderResponseDTO>> createOrder(
            @Valid @RequestBody OrderRequestDTO orderRequest) {
        log.info("Received order creation request for customer: {}", orderRequest.getCustomerName());

        OrderResponseDTO createdOrder = orderService.createOrder(orderRequest);

        ApiResponseWrapper<OrderResponseDTO> wrapper = ApiResponseWrapper.<OrderResponseDTO>builder()
                                                                         .success(true)
                                                                         .message("Order created successfully")
                                                                         .data(createdOrder)
                                                                         .build();

        return ResponseEntity.ok(wrapper);
    }

    @GetMapping
    @Operation(summary = "Get all orders", description = "Retrieves all orders with pagination support")
    public ResponseEntity<ApiResponseWrapper<PageResponseDTO<OrderResponseDTO>>> getAllOrders(
            @Parameter(description = "Page number (0-indexed)")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field")
            @RequestParam(defaultValue = "orderTime") String sortBy,
            @Parameter(description = "Sort direction (ASC/DESC)")
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        log.info("Fetching orders - page: {}, size: {}, sortBy: {}, direction: {}",
                page, size, sortBy, sortDirection);

        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        PageResponseDTO<OrderResponseDTO> orders = orderService.getAllOrders(pageable);

        return ResponseEntity.ok(ApiResponseWrapper.<PageResponseDTO<OrderResponseDTO>>builder()
                                                   .success(true)
                                                   .message("Orders retrieved successfully")
                                                   .data(orders)
                                                   .timeStamp(LocalDateTime.now())
                                                   .build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID", description = "Retrieves a specific order by its ID")
    public ResponseEntity<ApiResponseWrapper<OrderResponseDTO>> getOrderById(
            @PathVariable Long id) {
        log.info("Fetching order with ID: {}", id);

        OrderResponseDTO order = orderService.getOrderById(id);

        return ResponseEntity.ok(ApiResponseWrapper.<OrderResponseDTO>builder()
                                                   .success(true)
                                                   .message("Order retrieved successfully")
                                                   .data(order)
                                                   .timeStamp(LocalDateTime.now())
                                                   .build());
    }

    @GetMapping("/{id}/status")
    @Operation(summary = "Get order status", description = "Retrieves the current status of an order")
    public ResponseEntity<ApiResponseWrapper<OrderStatusResponseDTO>> getOrderStatus(
            @PathVariable Long id) {
        log.info("Fetching status for order: {}", id);

        OrderResponseDTO order = orderService.getOrderById(id);

        OrderStatusResponseDTO statusResponse = OrderStatusResponseDTO.builder()
                                                                      .orderId(order.getId())
                                                                      .status(order.getStatus())
                                                                      .orderTime(order.getOrderTime())
                                                                      .processedTime(order.getProcessedTime())
                                                                      .build();

        return ResponseEntity.ok(ApiResponseWrapper.<OrderStatusResponseDTO>builder()
                                                   .success(true)
                                                   .message("Order status retrieved successfully")
                                                   .data(statusResponse)
                                                   .timeStamp(LocalDateTime.now())
                                                   .build());
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update order status", description = "Manually updates the status of an order")
    public ResponseEntity<ApiResponseWrapper<OrderResponseDTO>> updateOrderStatus(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateDTO statusUpdate) {
        log.info("Updating status for order {} to: {}", id, statusUpdate.getStatus());

        OrderResponseDTO updatedOrder = orderService.updateOrderStatus(id, statusUpdate);

        return ResponseEntity.ok(ApiResponseWrapper.<OrderResponseDTO>builder()
                                                   .success(true)
                                                   .message("Order status updated successfully")
                                                   .data(updatedOrder)
                                                   .timeStamp(LocalDateTime.now())
                                                   .build());
    }
}
