package com.foodorderservice.Foodie.service.impl;


import com.foodorderservice.Foodie.dtos.*;
import com.foodorderservice.Foodie.entity.Order;
import com.foodorderservice.Foodie.entity.OrderItem;
import com.foodorderservice.Foodie.entity.enums.OrderStatus;
import com.foodorderservice.Foodie.exception.InvalidOrderStateException;
import com.foodorderservice.Foodie.exception.OrderNotFoundException;
import com.foodorderservice.Foodie.repository.OrderRepository;
import com.foodorderservice.Foodie.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ApplicationEventPublisher eventPublisher;

    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest) {
        log.info("Creating new order for customer: {}", orderRequest.getCustomerName());


        BigDecimal calculatedTotal = calculateTotalAmount(orderRequest.getItems());
        if (calculatedTotal.compareTo(orderRequest.getTotalAmount()) != 0) {
            throw new InvalidOrderStateException("Total amount does not match sum of items");
        }

        Order order = modelMapper.map(orderRequest, Order.class);
        order.setStatus(OrderStatus.PENDING);


        for (OrderItem item : order.getItems()) {
            item.setOrder(order);
        }

        Order savedOrder = orderRepository.save(order);
        log.info("Order created with ID: {}", savedOrder.getId());


        eventPublisher.publishEvent(new OrderCreatedEvent(savedOrder.getId()));

        return modelMapper.map(savedOrder, OrderResponseDTO.class);
    }


    public PageResponseDTO<OrderResponseDTO> getAllOrders(Pageable pageable) {
        log.info("Fetching orders - page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<Order> orderPage = orderRepository.findAll(pageable);

        List<OrderResponseDTO> orderDTOs = orderPage.getContent().stream()
                                                    .map(order -> modelMapper.map(order, OrderResponseDTO.class))
                                                    .collect(Collectors.toList());

        return PageResponseDTO.<OrderResponseDTO>builder()
                                              .content(orderDTOs)
                                              .pageNumber(orderPage.getNumber())
                                              .pageSize(orderPage.getSize())
                                              .totalElements(orderPage.getTotalElements())
                                              .totalPages(orderPage.getTotalPages())
                                              .first(orderPage.isFirst())
                                              .last(orderPage.isLast())
                                              .build();
    }


    public OrderResponseDTO getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);

        Order order = orderRepository.findByIdWithItems(id)
                                     .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));

        return modelMapper.map(order, OrderResponseDTO.class);
    }

    public OrderResponseDTO updateOrderStatus(Long id, OrderStatusUpdateDTO statusUpdate) {
        log.info("Updating order {} status to: {}", id, statusUpdate.getStatus());

        Order order = orderRepository.findById(id)
                                     .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + id));

        validateStatusTransition(order.getStatus(), statusUpdate.getStatus());

        order.setStatus(statusUpdate.getStatus());

        if (statusUpdate.getStatus() == OrderStatus.PROCESSED) {
            order.setProcessedTime(LocalDateTime.now());
        }

        Order updatedOrder = orderRepository.save(order);
        log.info("Order {} status updated successfully", id);

        return modelMapper.map(updatedOrder, OrderResponseDTO.class);
    }

    public void processOrder(Long orderId) {
        log.info("Processing order: {}", orderId);

        Order order = orderRepository.findById(orderId)
                                     .orElseThrow(() -> new OrderNotFoundException("Order not found with ID: " + orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            log.warn("Order {} is not in PENDING status, skipping processing", orderId);
            return;
        }

        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Order processing interrupted for order: {}", orderId);
            return;
        }

        order.setStatus(OrderStatus.PROCESSED);
        order.setProcessedTime(LocalDateTime.now());
        orderRepository.save(order);

        log.info("Order {} processed successfully", orderId);
    }

    private BigDecimal calculateTotalAmount(List<OrderItemDTO> items) {
        return items.stream()
                    .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateStatusTransition(OrderStatus currentStatus, OrderStatus newStatus) {

        boolean validTransition = switch (currentStatus) {
            case PENDING -> newStatus == OrderStatus.PROCESSING ||
                    newStatus == OrderStatus.CANCELLED;
            case PROCESSING -> newStatus == OrderStatus.PROCESSED ||
                    newStatus == OrderStatus.CANCELLED;
            case PROCESSED -> newStatus == OrderStatus.DELIVERED;
            case DELIVERED, CANCELLED -> false;
        };

        if (!validTransition) {
            throw new InvalidOrderStateException(
                    String.format("Invalid status transition from %s to %s", currentStatus, newStatus)
            );
        }
    }
}

