package com.example.tradingsystem.config;

import com.example.tradingsystem.dto.RestClientConfigSettings;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {
    private final RestClientConfigSettings restClientConfigSettings;

    @Bean
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder().requestFactory(new HttpComponentsClientHttpRequestFactory(httpClient()));
    }

    private HttpClient httpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(restClientConfigSettings.maxConnTotal());
        connectionManager.setDefaultMaxPerRoute(restClientConfigSettings.maxConnPerRoute());

        return HttpClientBuilder.create().setConnectionManager(connectionManager)
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(restClientConfigSettings.connectTimeoutMs(), TimeUnit.MILLISECONDS)
                        .setResponseTimeout(restClientConfigSettings.readTimeoutMs(), TimeUnit.MILLISECONDS).build()).build();
    }
}
