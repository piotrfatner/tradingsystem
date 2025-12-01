package com.example.tradingsystem.event;

import com.example.tradingsystem.enumes.OrderStatus;

public record OrderEvent(Long orderId, OrderStatus status) {
}
