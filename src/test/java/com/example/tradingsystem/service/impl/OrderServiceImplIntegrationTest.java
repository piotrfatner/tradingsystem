package com.example.tradingsystem.service.impl;

import com.example.tradingsystem.dto.OrderDto;
import com.example.tradingsystem.enumes.OrderStatus;
import com.example.tradingsystem.enumes.OrderType;
import com.example.tradingsystem.enumes.TransactionSide;
import com.example.tradingsystem.repository.OrderRepository;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 9000)
@TestPropertySource(properties = {
        "gpw.base-url=http://localhost:9000"
})
public class OrderServiceImplIntegrationTest {

    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private OrderRepository orderRepository;


    @BeforeEach
    void setUp() {
        WireMock.reset();

    }

    /*@Test
    void fetchOrderByOrderId_ShouldCallExternalSystemAndReturnOrder() {
        // given
        Long orderId = 100L;

        stubFor(get(urlEqualTo("/gpw/order/" + orderId))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                            {
                                "orderId": 1111111,
                                "status": "Filled",
                                "isin": "PLBSK0000017",
                                "side": "BUY",
                                "tradeCurrency": "PLN",
                                "quantity": 10,
                                "executionPrice": 315,
                                "registrationTime": 1762444418,
                                "executedTime": 1762448027
                            }
                        """))); // Adjust JSON fields to match your OrderDto

        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(1111111L);
        orderDto.setStatus(OrderStatus.FILLED);
        orderDto.setIsin("PLBSK0000017");
        orderDto.setSide(TransactionSide.BUY);
        orderDto.setTradeCurrency("PLN");
        orderDto.setQuantity(10);
        orderDto.setExecutionPrice(new BigDecimal(315));
        orderDto.setRegistrationTime(Instant.ofEpochMilli(1762444418));
        orderDto.setExecutedTime(Instant.ofEpochMilli(1762448027));

        //when
        OrderDto result = orderService.fetchOrderByOrderId(orderId);

        // then
        assertNotNull(result);
        assertEquals(orderDto.getOrderId(), result.getOrderId());
        // assertEquals(100L, result.getId()); // Uncomment once DTO has fields
    }*/

    @Test
    void createOrder_ShouldSendRequestToExternalSystemAndSaveOrderToDb() {
        //given
        OrderDto inputOrderDto = new OrderDto();
        inputOrderDto.setIsin("PLBSK0000017");
        inputOrderDto.setSide(TransactionSide.BUY);
        inputOrderDto.setQuantity(10);
        inputOrderDto.setLimitPrice(new BigDecimal("315.00"));
        inputOrderDto.setOrderType(OrderType.LMT);
        inputOrderDto.setTradeCurrency("PLN");

        // Mock external service response
        stubFor(post(urlEqualTo("/gpw/orders"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody("""
                            {
                                "orderId": 99999,
                                "status": "SUBMITTED",
                                "registrationTime": "2025-12-01T00:00:00Z"
                            }
                        """)));

        //when
        OrderDto result = orderService.createOrder(inputOrderDto);
        List<OrderDto> ordersDtos = orderService.fetchAllOrders();

        // then
        assertNotNull(result);
        assertEquals(99999L, result.getExternalOrderId());
        assertEquals(OrderStatus.SUBMITTED, result.getStatus());

        verify(postRequestedFor(urlEqualTo("/gpw/orders"))
                .withRequestBody(matchingJsonPath("$.isin", equalTo("PLBSK0000017")))
                .withRequestBody(matchingJsonPath("$.quantity", equalTo("10")))
                .withRequestBody(matchingJsonPath("$.side", equalTo("BUY"))));

        assertEquals(1, ordersDtos.size());
        OrderDto savedOrderDto = ordersDtos.get(0);
        assertEquals(99999L, savedOrderDto.getExternalOrderId());
        assertEquals("PLBSK0000017", savedOrderDto.getIsin());
        assertNotNull(savedOrderDto.getOrderId());
    }
}
