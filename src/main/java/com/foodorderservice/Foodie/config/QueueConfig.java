package com.foodorderservice.Foodie.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Configuration
public class QueueConfig {

    @Value("${queue.capacity:1000}")
    private int queueCapacity;

    @Bean
    public InMemoryQueue orderQueue() {
        return new InMemoryQueue(queueCapacity);
    }

    public static class InMemoryQueue {
        private final BlockingQueue<Long> queue;

        public InMemoryQueue(int capacity) {
            this.queue = new LinkedBlockingQueue<>(capacity);
        }

        public boolean offer(Long orderId, long timeout, TimeUnit unit) throws InterruptedException {
            return queue.offer(orderId, timeout, unit);
        }

        public void put(Long orderId) throws InterruptedException {
            queue.put(orderId);
        }

        public Long take() throws InterruptedException {
            return queue.take();
        }

    }
}
