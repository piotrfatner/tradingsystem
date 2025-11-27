package com.example.tradingsystem.service;

import com.example.tradingsystem.dto.OrderDto;

import java.util.List;

public interface IOrderService {

    List<OrderDto> fetchAllOrders();

    OrderDto fetchOrderByOrderId(Long orderId);

    OrderDto createOrder(OrderDto orderDto);
}
