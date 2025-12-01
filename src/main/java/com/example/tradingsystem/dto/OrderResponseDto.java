package com.example.tradingsystem.dto;

import com.example.tradingsystem.enumes.OrderStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class OrderResponseDto {
    private Long orderId;
    private OrderStatus status;
    private Instant registrationTime;
}
