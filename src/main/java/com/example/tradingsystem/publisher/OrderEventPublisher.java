package com.example.tradingsystem.publisher;

import com.example.tradingsystem.event.OrderEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OrderEventPublisher {

    private final KafkaTemplate<String, OrderEvent> kafkaTemplate;
    private static final String TOPIC_ORDERS_FILLED = "orders.filled";

    public OrderEventPublisher(KafkaTemplate<String, OrderEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishOrderFilled(OrderEvent event) {
        log.info("Publishing to Kafka topic {}: Order {} is FILLED", TOPIC_ORDERS_FILLED, event);
        kafkaTemplate.send(TOPIC_ORDERS_FILLED, event);
    }
}
