package com.health.remind.config.lock;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author QQQtx
 * @since 2025/2/7 15:35
 */
@Configuration
public class RedissonConfig {

    @Bean
    public RedissonClient redisSonClient(RedissonProperties properties) {
        Config config = new Config();
        config.useSingleServer()
                .setClientName("redisson-lock")
                .setAddress("redis://" + properties.getHost() + ":" + properties.getPort())
                .setPassword(properties.getPassword())
                .setDatabase(properties.getDatabase());
        return Redisson.create(config);
    }
}
