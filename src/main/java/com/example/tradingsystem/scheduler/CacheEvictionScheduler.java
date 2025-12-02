package com.example.tradingsystem.scheduler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@AllArgsConstructor
public class CacheEvictionScheduler {
    private final CacheManager cacheManager;

    // Every 15 minutes it clears caches
    @Scheduled(fixedRate = 900000)
    public void evictAllCaches() {
        log.info("Evicting all caches");
        cacheManager.getCacheNames()
                .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName)).clear());
    }
}
