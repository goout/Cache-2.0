package com.taraskin.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class CacheConfiguration {

    private static final Long RECORDS_MAX_COUNT = 5L;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCache recordsCache = new CaffeineCache("records", Caffeine.newBuilder()
                .maximumSize(RECORDS_MAX_COUNT)
                .build());
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Collections.singletonList(recordsCache));
        return cacheManager;
    }


}
