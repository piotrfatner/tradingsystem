package com.example.tradingsystem.service.impl;

import com.example.tradingsystem.dto.OrderDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void fetchOrderByOrderIdShouldReturnOrder() {
        //given
        Long orderId = 1L;

        //when
        OrderDto order = orderService.fetchOrderByOrderId(orderId);

        //then
        assertNotNull(order, "Service should not return null");
    }
}
