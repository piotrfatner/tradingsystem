package com.example.tradingsystem.service.impl;

import com.example.tradingsystem.dto.OrderDto;
import com.example.tradingsystem.service.IOrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements IOrderService {
    @Override
    public List<OrderDto> fetchAllOrders() {
        return List.of();
    }

    @Override
    public OrderDto fetchOrderByOrderId(Long orderId) {
        return null;
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        return null;
    }
}
