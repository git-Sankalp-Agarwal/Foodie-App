package com.foodorderservice.Foodie.config;

import com.foodorderservice.Foodie.entity.enums.OrderStatus;
import com.foodorderservice.Foodie.repository.OrderRepository;
import com.foodorderservice.Foodie.service.impl.QueueService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PendingOrderRequeuer {
    private final OrderRepository orderRepository;
    private final QueueService queueService;

    @PostConstruct
    public void requeuePendingOrders() {
        orderRepository.findByStatus(OrderStatus.PENDING).forEach(o -> {
            try {
                queueService.sendOrderForProcessing(o.getId());
            } catch (Exception e) {
                log.error("Failed to requeue order {}", o.getId(), e);
            }
        });
    }
}
