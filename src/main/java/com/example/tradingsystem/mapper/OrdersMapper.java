package com.example.tradingsystem.mapper;

import com.example.tradingsystem.dto.OrderDto;
import com.example.tradingsystem.dto.OrderRequestDto;
import com.example.tradingsystem.entity.TradeOrder;

public class OrdersMapper {

    public static OrderRequestDto mapOrderToOrderRequestDto(OrderDto orderDto, OrderRequestDto orderRequestDto) {
        orderRequestDto.setAccountId(orderDto.getAccountId());
        orderRequestDto.setIsin(orderDto.getIsin());
        orderRequestDto.setSide(orderDto.getSide());
        orderRequestDto.setTradeCurrency(orderDto.getTradeCurrency());
        orderRequestDto.setQuantity(orderDto.getQuantity());
        orderRequestDto.setExpiresAt(orderDto.getExpiresAt());
        orderRequestDto.setOrderType(orderDto.getOrderType());
        orderRequestDto.setLimitPrice(orderDto.getLimitPrice());
        return orderRequestDto;
    }

    public static TradeOrder mapOrderToTradeOrder(OrderDto orderDto, TradeOrder tradeOrder) {
        tradeOrder.setAccountId(orderDto.getAccountId());
        tradeOrder.setExternalOrderId(orderDto.getExternalOrderId());
        tradeOrder.setIsin(orderDto.getIsin());
        tradeOrder.setSide(orderDto.getSide());
        tradeOrder.setTradeCurrency(orderDto.getTradeCurrency());
        tradeOrder.setQuantity(orderDto.getQuantity());
        tradeOrder.setExpiresAt(orderDto.getExpiresAt());
        tradeOrder.setOrderType(orderDto.getOrderType());
        tradeOrder.setLimitPrice(orderDto.getLimitPrice());
        tradeOrder.setStatus(orderDto.getStatus());
        tradeOrder.setRegistrationTime(orderDto.getRegistrationTime());
        return tradeOrder;
    }

    public static OrderDto mapTradeOrderToOrderDto(TradeOrder tradeOrder) {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(tradeOrder.getOrderId());
        orderDto.setExternalOrderId(tradeOrder.getExternalOrderId());
        orderDto.setAccountId(tradeOrder.getAccountId());
        orderDto.setIsin(tradeOrder.getIsin());
        orderDto.setSide(tradeOrder.getSide());
        orderDto.setTradeCurrency(tradeOrder.getTradeCurrency());
        orderDto.setQuantity(tradeOrder.getQuantity());
        orderDto.setExpiresAt(tradeOrder.getExpiresAt());
        orderDto.setOrderType(tradeOrder.getOrderType());
        orderDto.setLimitPrice(tradeOrder.getLimitPrice());
        orderDto.setStatus(tradeOrder.getStatus());
        orderDto.setRegistrationTime(tradeOrder.getRegistrationTime());
        orderDto.setExecutedTime(tradeOrder.getExecutedTime());
        orderDto.setExecutionPrice(tradeOrder.getExecutionPrice());
        orderDto.setCommission(tradeOrder.getCommission());
        return orderDto;
    }
}
