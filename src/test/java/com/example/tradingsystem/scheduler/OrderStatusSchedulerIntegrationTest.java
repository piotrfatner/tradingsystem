package com.example.tradingsystem.scheduler;

import com.example.tradingsystem.entity.TradeOrder;
import com.example.tradingsystem.enumes.OrderStatus;
import com.example.tradingsystem.enumes.OrderType;
import com.example.tradingsystem.enumes.TransactionSide;
import com.example.tradingsystem.event.OrderEvent;
import com.example.tradingsystem.repository.OrderRepository;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.apache.kafka.clients.consumer.Consumer;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 9000)
@EmbeddedKafka(partitions = 1, topics = {"orders.filled"})
@DirtiesContext
@TestPropertySource(properties = {"gpw.base-url=http://localhost:9000", "spring.kafka.bootstrap-servers=$" +
        "{spring.embedded.kafka.brokers}"})
public class OrderStatusSchedulerIntegrationTest {
    @Autowired
    private OrderStatusScheduler orderStatusScheduler;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    private Consumer<String, OrderEvent> consumer;


    @BeforeEach
    void setUp() {
        WireMock.reset();
        orderRepository.deleteAll(); // Necessary?

        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true",
                embeddedKafkaBroker);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        DefaultKafkaConsumerFactory<String, OrderEvent> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(),
                        new JsonDeserializer<>(OrderEvent.class, false));
        consumer = consumerFactory.createConsumer();
        consumer.subscribe(Collections.singletonList("orders.filled"));
    }

    @Test
    void updateOrderStatuses_ShouldUpdateStatusAndPublishEvent_WhenOrderIsFilled() throws InterruptedException {
        //given: An order in the DB with SUBMITTED status
        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setExternalOrderId(12345L);
        tradeOrder.setAccountId(1L);
        tradeOrder.setIsin("PLBSK0000017");
        tradeOrder.setSide(TransactionSide.BUY);
        tradeOrder.setQuantity(10);
        tradeOrder.setOrderType(OrderType.LMT);
        tradeOrder.setStatus(OrderStatus.SUBMITTED);
        tradeOrder.setMic("XWAR");
        tradeOrder = orderRepository.save(tradeOrder);

        stubFor(get(urlEqualTo("/gpw/order/12345")).willReturn(aResponse().withStatus(200).withHeader(
                "Content-Type", MediaType.APPLICATION_JSON_VALUE).withBody("""
                    {
                        "orderId": 12345,
                        "status": "FILLED", 
                        "executionPrice": 315.50,
                        "executedTime": "2025-12-01T00:00:00Z"
                    }
                """)));

        //when: OrderStatusScheduler is called
        orderStatusScheduler.checkSubmittedOrders();
        //Thread.sleep(11_000); for testing scheduler

        //then
        TradeOrder updatedOrder = orderRepository.findById(tradeOrder.getOrderId()).orElseThrow();
        assertEquals(OrderStatus.FILLED, updatedOrder.getStatus());
        assertEquals(new BigDecimal("315.50"), updatedOrder.getExecutionPrice());

        ConsumerRecords<String, OrderEvent> records = KafkaTestUtils.getRecords(consumer);
        assertEquals(1, records.count());
        OrderEvent event = records.iterator().next().value();
        assertEquals(tradeOrder.getOrderId(), event.orderId());
        assertEquals(OrderStatus.FILLED, event.status());
    }

    @Test
    void updateOrderStatuses_ShouldUpdateStatus_WhenOrderIsExpired() throws InterruptedException {
        // given
        TradeOrder tradeOrder = new TradeOrder();
        tradeOrder.setExternalOrderId(98765L);
        tradeOrder.setAccountId(1L);
        tradeOrder.setIsin("PLBSK0000017");
        tradeOrder.setSide(TransactionSide.SELL);
        tradeOrder.setQuantity(5);
        tradeOrder.setOrderType(OrderType.LMT);
        tradeOrder.setStatus(OrderStatus.SUBMITTED);
        tradeOrder.setMic("XWAR");
        tradeOrder.setExpiresAt(Instant.now().plusSeconds(1000));
        tradeOrder = orderRepository.save(tradeOrder);

        stubFor(get(urlEqualTo("/gpw/order/98765")).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .withBody("""
                        {
                            "orderId": 98765,
                            "status": "EXPIRED",
                            "executionPrice": null,
                            "executedTime": null
                        }
                    """)));

        // when
        orderStatusScheduler.checkSubmittedOrders();

        // then
        TradeOrder updatedOrder = orderRepository.findById(tradeOrder.getOrderId()).orElseThrow();
        assertEquals(OrderStatus.EXPIRED, updatedOrder.getStatus());

        // Verify NO Kafka event is published for EXPIRED status
        ConsumerRecords<String, OrderEvent> records = KafkaTestUtils.getRecords(consumer, java.time.Duration.ofMillis(500));
        assertEquals(0, records.count(), "Should not publish event for expired order");
    }

}
