package com.health.remind.config.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.health.remind.common.StaticConstant;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author QQQtx
 * @since 2025/2/21
 */
@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(Arrays.asList(StaticConstant.CACHE_10, StaticConstant.CACHE_5));
        cacheManager.registerCustomCache(StaticConstant.CACHE_10, caffeineCacheBuilder1());
        cacheManager.registerCustomCache(StaticConstant.CACHE_5, caffeineCacheBuilder2());
        return cacheManager;
    }

    @Bean
    public Cache<Object, Object> caffeineCacheBuilder1() {
        return Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .build();
    }

    @Bean
    public Cache<Object, Object> caffeineCacheBuilder2() {
        return Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
    }
}
