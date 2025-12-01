package com.example.tradingsystem.consumer;

import com.example.tradingsystem.event.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventConsumer {
    @KafkaListener(topics = "orders.filled", groupId = "trading-system-group")
    public void consumeOrderFilled(OrderEvent event) {
        log.info("Event is sent via email to client! Order {} is now {}", event.orderId(), event.status());
    }
}
