package com.example.tradingsystem.controller;

import com.example.tradingsystem.dto.OrderDto;
import com.example.tradingsystem.service.IOrderService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/orders", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class OrderController {
    private IOrderService iOrderService;

    @GetMapping()
    public ResponseEntity<List<OrderDto>> fetchAllOrders() {
        List<OrderDto> allOrdersList = iOrderService.fetchAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(allOrdersList);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> fetchOrderByOrderId(@PathVariable("orderId") @Positive(message = "Order" +
            " ID must be positive") Long orderId) {
        OrderDto order = iOrderService.fetchOrderByOrderId(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @PostMapping()
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        OrderDto createdOrder = iOrderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }
}
