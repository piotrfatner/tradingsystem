package com.example.tradingsystem.controller;

import com.example.tradingsystem.dto.OrderDto;
import com.example.tradingsystem.dto.errors.ErrorResponseDto;
import com.example.tradingsystem.service.IOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Orders", description = "CRUD REST APIs to CREATE and FETCH trading orders.")
@RestController
@RequestMapping(path = "/api/orders", produces = {MediaType.APPLICATION_JSON_VALUE})
@AllArgsConstructor
@Validated
public class OrderController {
    private IOrderService iOrderService;

    @Operation(summary = "Fetch List of Orders REST API", description = "REST API to fetch list of Orders in local user DB")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content =
            @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @GetMapping()
    public ResponseEntity<List<OrderDto>> fetchAllOrders() {
        List<OrderDto> allOrdersList = iOrderService.fetchAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(allOrdersList);
    }

    @Operation(summary = "Fetch Order by orderId REST API", description = "REST API to fetch Order based on orderId in local user DB")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content =
            @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> fetchOrderByOrderId(@PathVariable("orderId") @Positive(message = "Order" +
            " ID must be positive") Long orderId) {
        OrderDto order = iOrderService.fetchOrderByOrderId(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    @Operation(summary = "Create Order REST API", description = "REST API to send and create new Order in GPW and " +
            "create new Order in local user DB.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content =
            @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    @PostMapping()
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody OrderDto orderDto) {
        OrderDto createdOrder = iOrderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }
}
