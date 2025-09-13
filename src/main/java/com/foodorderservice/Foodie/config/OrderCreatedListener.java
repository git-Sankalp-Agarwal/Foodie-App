package com.foodorderservice.Foodie.config;

import com.foodorderservice.Foodie.dtos.OrderCreatedEvent;
import com.foodorderservice.Foodie.service.impl.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class OrderCreatedListener {
    private final QueueService queueService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCreated(OrderCreatedEvent event) {
        queueService.sendOrderForProcessing(event.orderId());
    }
}
