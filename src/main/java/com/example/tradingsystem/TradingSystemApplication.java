package com.example.tradingsystem;

import com.example.tradingsystem.dto.AccountInfo;
import com.example.tradingsystem.dto.RestClientConfigSettings;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties( {AccountInfo.class, RestClientConfigSettings.class})
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@EnableScheduling
@EnableCaching
@OpenAPIDefinition(
        info = @Info(title = "Trading System",
                version = "1.0",
                description = "POC of trading System API that connect to WireMock GPW external system."))
public class TradingSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingSystemApplication.class, args);
	}

}
