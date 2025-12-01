package com.example.tradingsystem.scheduler;

import com.example.tradingsystem.client.GpwClient;
import com.example.tradingsystem.dto.OrderResponseDetailsDto;
import com.example.tradingsystem.entity.TradeOrder;
import com.example.tradingsystem.enumes.OrderStatus;
import com.example.tradingsystem.event.OrderEvent;
import com.example.tradingsystem.publisher.OrderEventPublisher;
import com.example.tradingsystem.repository.OrderRepository;
import com.example.tradingsystem.utils.CommissionCalculatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Slf4j
@Service
public class OrderStatusScheduler {
    private final OrderRepository orderRepository;
    private final GpwClient gpwClient;
    private final OrderEventPublisher orderEventPublisher;

    public OrderStatusScheduler(OrderRepository orderRepository,
                                GpwClient gpwClient,
                                OrderEventPublisher orderEventPublisher) {
        this.orderRepository = orderRepository;
        this.gpwClient = gpwClient;
        this.orderEventPublisher = orderEventPublisher;
    }

    @Scheduled(fixedDelay = 10_000)
    public void checkSubmittedOrders() {
        List<TradeOrder> submittedOrders = orderRepository.findByStatus(OrderStatus.SUBMITTED);

        if(CollectionUtils.isEmpty(submittedOrders)) {
            return;
        }

        // TODO: Add logging here and in other locations!

        for(TradeOrder tradeOrder : submittedOrders) {
            handleOrder(tradeOrder);
        }
    }

    private void handleOrder(TradeOrder tradeOrder) {
        OrderResponseDetailsDto orderResponseDetails =
                gpwClient.fetchOrderDetailsByOrderId(tradeOrder.getExternalOrderId());

        switch (orderResponseDetails.getStatus().toUpperCase()) {
            case "FILLED":
                handleFilled(tradeOrder, orderResponseDetails);
                break;
            case "EXPIRED":
                handleExpired(tradeOrder);
                break;
            default:
                log.info("Order {} still in submitted status.", tradeOrder.getOrderId());

        }
    }

    private void handleFilled(TradeOrder tradeOrder, OrderResponseDetailsDto orderResponseDetails) {
        tradeOrder.setStatus(OrderStatus.FILLED);
        tradeOrder.setExecutionPrice(orderResponseDetails.getExecutionPrice());
        tradeOrder.setExecutedTime(orderResponseDetails.getExecutedTime());

        // Calculating commission
        BigDecimal calculatedCommission =
                CommissionCalculatorUtil.getCalculatedCommission(tradeOrder.getMic(),
                        tradeOrder.getExecutionPrice(), tradeOrder.getQuantity());
        tradeOrder.setCommission(calculatedCommission);
        orderRepository.save(tradeOrder);

        log.info("Order {} filled.", tradeOrder.getOrderId());

        orderEventPublisher.publishOrderFilled(new OrderEvent(tradeOrder.getOrderId(), OrderStatus.FILLED));
    }

    private void handleExpired(TradeOrder tradeOrder) {
        if(Instant.now().isBefore(tradeOrder.getExpiresAt())) {
            tradeOrder.setStatus(OrderStatus.EXPIRED);
            orderRepository.save(tradeOrder);
            log.info("Order {} expired by timeout", tradeOrder.getOrderId());
            return;
        }
        tradeOrder.setStatus(OrderStatus.EXPIRED);
        orderRepository.save(tradeOrder);
        log.info("Order {} expired by expiration date", tradeOrder.getOrderId());
    }
}
