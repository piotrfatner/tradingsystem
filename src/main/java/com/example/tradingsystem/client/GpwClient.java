package com.example.tradingsystem.client;

import com.example.tradingsystem.dto.*;
import com.example.tradingsystem.enumes.OrderStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class GpwClient {

    private final RestClient restClient;

    public GpwClient(RestClient.Builder builder, @Value("${gpw.base-url}") String gpwBaseUrl) {
        this.restClient = builder.baseUrl(gpwBaseUrl).build();
    }

    public List<InstrumentDto> fetchAllInstrumentsFromGpw() {
        InstrumentsTickersListDto instrumentsTickersListDto = restClient.get().uri("/gpw/tickers").retrieve()
                .body(InstrumentsTickersListDto.class);
        if(instrumentsTickersListDto != null && !CollectionUtils.isEmpty(instrumentsTickersListDto.getResults())) {
            return instrumentsTickersListDto.getResults();
        }
        return List.of();
    }

    public List<InstrumentPriceDto> fetchAllInstrumentsPrices() {
        InstrumentsPricesListDto instrumentsPricesListDto = restClient.get().uri("/gpw/prices/current").retrieve()
                .body(InstrumentsPricesListDto.class);
        if(instrumentsPricesListDto != null && !CollectionUtils.isEmpty(instrumentsPricesListDto.getResults())) {
            return instrumentsPricesListDto.getResults();
        }
        return List.of();
    }

    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto) {
        return restClient.post().uri("/gpw/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .body(orderRequestDto)
                .retrieve()
                .body(OrderResponseDto.class);
        /*OrderResponseDto orderResponseDto = new OrderResponseDto();
        orderResponseDto.setOrderId(1L);
        orderResponseDto.setStatus(OrderStatus.SUBMITTED);
        orderResponseDto.setRegistrationTime(Instant.now());
        return orderResponseDto;*/
    }

    public OrderResponseDetailsDto fetchOrderDetailsByOrderId(Long orderId) {
        return restClient.get().uri("/gpw/order/"+orderId).retrieve().body(OrderResponseDetailsDto.class);
    }
}
