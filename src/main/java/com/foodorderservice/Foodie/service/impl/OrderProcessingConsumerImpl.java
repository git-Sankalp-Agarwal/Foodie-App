package com.foodorderservice.Foodie.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderProcessingConsumerImpl {

    private final QueueService queueService;
    private final OrderServiceImpl orderServiceImpl;
    private volatile boolean running = true;

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void startProcessing() {
        log.info("Order processing consumer started");

        while (running) {
            try {
                Long orderId = queueService.receiveOrder();
                log.info("Received order {} from queue for processing", orderId);

                try {
                    orderServiceImpl.processOrder(orderId);
                } catch (Exception e) {
                    log.error("Error processing order {}: {}", orderId, e.getMessage());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.info("Order processing consumer interrupted");
                break;
            }
        }
    }

    public void stop() {
        running = false;
    }
}
