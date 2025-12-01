package com.example.tradingsystem.service.impl;

import com.example.tradingsystem.client.GpwClient;
import com.example.tradingsystem.dto.*;
import com.example.tradingsystem.entity.TradeOrder;
import com.example.tradingsystem.enumes.OrderStatus;
import com.example.tradingsystem.enumes.OrderType;
import com.example.tradingsystem.exception.CurrencyNotMatchingException;
import com.example.tradingsystem.exception.LimitPriceForLimitOrderNotFoundException;
import com.example.tradingsystem.exception.ResourceNotFoundException;
import com.example.tradingsystem.mapper.OrdersMapper;
import com.example.tradingsystem.repository.OrderRepository;
import com.example.tradingsystem.service.IInstrumentService;
import com.example.tradingsystem.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    private final GpwClient gpwClient;
    private final OrderRepository orderRepository;
    private final IInstrumentService instrumentService;

    @Autowired
    private AccountInfo accountInfo;

    public OrderServiceImpl(GpwClient gpwClient, OrderRepository orderRepository,
                            IInstrumentService instrumentService) {
        this.gpwClient = gpwClient;
        this.orderRepository = orderRepository;
        this.instrumentService = instrumentService;
    }

    @Override
    public List<OrderDto> fetchAllOrders() {
        List<TradeOrder> tradeOrders = orderRepository.findAll();
        return tradeOrders.stream().map(OrdersMapper::mapTradeOrderToOrderDto).toList();
    }

    @Override
    public OrderDto fetchOrderByOrderId(Long orderId) {
        return orderRepository.findByOrderId(orderId).map(OrdersMapper::mapTradeOrderToOrderDto).orElseThrow(() -> new ResourceNotFoundException("Order", "orderId", orderId.toString()));
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        validateOrder(orderDto);

        OrderRequestDto orderRequestDto = createOrderRequestDto(orderDto);
        orderDto.setAccountId(accountInfo.accountId());

        OrderResponseDto orderResponseDto = gpwClient.createOrder(orderRequestDto);
        if (orderResponseDto != null) {
            orderDto.setExternalOrderId(orderResponseDto.getOrderId());
            orderDto.setStatus(orderResponseDto.getStatus());
            orderDto.setRegistrationTime(orderResponseDto.getRegistrationTime());
        }

        TradeOrder tradeOrder = new TradeOrder();
        OrdersMapper.mapOrderToTradeOrder(orderDto, tradeOrder);
        tradeOrder.setMic("XWAR"); // Right now is hardcoded
        TradeOrder savedTradeOrder = orderRepository.save(tradeOrder);
        orderDto.setOrderId(savedTradeOrder.getOrderId());
        return orderDto;
    }

    private void validateOrder(OrderDto orderDto) {
        // Validate limit price for Limit order
        if (orderDto.getOrderType() == OrderType.LMT && orderDto.getLimitPrice() == null) {
            throw new LimitPriceForLimitOrderNotFoundException(orderDto.getIsin());
        }

        InstrumentDto instrument = instrumentService.fetchInstrumentByIsin(orderDto.getIsin());

        // Validate if ISIN exists in GPW instruments
        if (instrument == null) {
            throw new ResourceNotFoundException("Instrument", "isin", orderDto.getIsin());
        }

        // Validate if currency matches
        if (!instrument.getTradeCurrency().equals(orderDto.getTradeCurrency())) {
            throw new CurrencyNotMatchingException(orderDto.getTradeCurrency(),
                    instrument.getTradeCurrency());
        }


    }

    private OrderRequestDto createOrderRequestDto(OrderDto orderDto) {
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setAccountId(accountInfo.accountId());
        OrdersMapper.mapOrderToOrderRequestDto(orderDto, orderRequestDto);
        return orderRequestDto;
    }
}
