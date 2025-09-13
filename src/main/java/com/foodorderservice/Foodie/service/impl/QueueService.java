package com.foodorderservice.Foodie.service.impl;


import com.foodorderservice.Foodie.config.QueueConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueueService {

    private final QueueConfig.InMemoryQueue queue;


    public void sendOrderForProcessing(Long orderId) {
        log.info("Sending order {} to processing queue", orderId);
        int attempts = 0;
        boolean added = false;
        while (!added && attempts++ < 3) {
            try {
                added = queue.offer(orderId, 1, TimeUnit.SECONDS);
                if (!added) {
                    log.warn("Queue full, retry {} for order {}", attempts, orderId);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Interrupted while enqueuing order {}", orderId, e);
                return;
            }
        }
        if (!added) {
            log.error("Failed to enqueue order {} after retries", orderId);
        }
    }

    public Long receiveOrder() throws InterruptedException {
        return queue.take();
    }
}
