package com.health.remind.config.lock;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author QQQtx
 * @since 2025/2/7 15:42
 */
@ConfigurationProperties(prefix = "spring.data.redis")
@Component
@Data
public class RedissonProperties {

    private String host;

    private int port;

    private String password;

    private int database;
}
