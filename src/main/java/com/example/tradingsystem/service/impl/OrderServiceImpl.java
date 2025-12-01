package com.example.tradingsystem.service.impl;

import com.example.tradingsystem.client.GpwClient;
import com.example.tradingsystem.dto.AccountInfo;
import com.example.tradingsystem.dto.OrderDto;
import com.example.tradingsystem.dto.OrderRequestDto;
import com.example.tradingsystem.dto.OrderResponseDto;
import com.example.tradingsystem.entity.TradeOrder;
import com.example.tradingsystem.mapper.OrdersMapper;
import com.example.tradingsystem.repository.OrderRepository;
import com.example.tradingsystem.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    private final GpwClient gpwClient;
    private final OrderRepository orderRepository;

    @Autowired
    private AccountInfo accountInfo;

    public OrderServiceImpl(GpwClient gpwClient, OrderRepository orderRepository) {
        this.gpwClient = gpwClient;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<OrderDto> fetchAllOrders() {
        List<TradeOrder> tradeOrders = orderRepository.findAll();
        return tradeOrders.stream().map(OrdersMapper::mapTradeOrderToOrderDto).toList();
    }

    @Override
    public OrderDto fetchOrderByOrderId(Long orderId) {
        return orderRepository.findById(orderId).map(OrdersMapper::mapTradeOrderToOrderDto).orElse(null);
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        // TODO: 1. Should validate buying
        //  2. Should handle custom RuntimeException

        OrderRequestDto orderRequestDto = createOrderRequestDto(orderDto);
        orderDto.setAccountId(accountInfo.accountId());

        OrderResponseDto orderResponseDto = gpwClient.createOrder(orderRequestDto);
        if(orderResponseDto != null) {
            orderDto.setExternalOrderId(orderResponseDto.getOrderId());
            orderDto.setStatus(orderResponseDto.getStatus());
            orderDto.setRegistrationTime(orderResponseDto.getRegistrationTime());
        }
        // TODO: Save to DB in order to later check this in cron
        TradeOrder tradeOrder = new TradeOrder();
        OrdersMapper.mapOrderToTradeOrder(orderDto, tradeOrder);
        orderRepository.save(tradeOrder);
        // TODO: Should add orderId to orderDto?
        return orderDto; // TODO: Should I return orderDto?
    }

    private OrderRequestDto createOrderRequestDto(OrderDto orderDto) {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setAccountId(accountInfo.accountId());
        OrdersMapper.mapOrderToOrderRequestDto(orderDto, orderRequestDto);
        return orderRequestDto;
    }
}
