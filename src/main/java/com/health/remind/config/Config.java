package com.health.remind.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.health.remind.config.enums.UserInfo;
import lombok.NonNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author QQQtx
 * @since 2024/11/26
 */
@EnableAsync
@Configuration
public class Config {

    /**
     * 密码解密方式
     *
     * @return 密码加密对象
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ObjectMapper objectMapper() {
        DateTimeFormatter allFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(allFormatter));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(allFormatter));
        module.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        module.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));
        module.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        module.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.registerModule(new SimpleModule().addSerializer(Long.class, ToStringSerializer.instance));
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        return mapper;
    }

    /**
     * 这里定义 Spring 管理的 Executor
     *
     * @param pool
     * @return
     */
    @Bean("customExecutor")
    public Executor executor(ThreadPoolConfigProperties pool) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(pool.getCoreSize());
        executor.setMaxPoolSize(pool.getMaxSize());
        executor.setQueueCapacity(1000);
        executor.setKeepAliveSeconds(pool.getKeepAliveTime());
        executor.setThreadNamePrefix("AsyncExecutor-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        executor.setTaskDecorator(new ContextCopyingDecorator());
        executor.initialize();
        return executor;
    }

    private static class ContextCopyingDecorator implements TaskDecorator {
        @Override
        @NonNull
        public Runnable decorate(@NonNull Runnable runnable) {
            Map<UserInfo, String> contextMap = CommonMethod.getMap();
            return () -> {
                try {
                    CommonMethod.setMap(contextMap);
                    runnable.run();
                } finally {
                    CommonMethod.clear();
                }
            };
        }
    }

    @Bean
    public ThreadPoolExecutor threadPoolExecutor(ThreadPoolConfigProperties pool) {
        return new ThreadPoolExecutor(
                pool.getCoreSize(),
                pool.getMaxSize(),
                pool.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(1000),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    @Bean
    public ScheduledThreadPoolExecutor scheduledThreadPoolExecutor(ThreadPoolConfigProperties pool) {
        return new ScheduledThreadPoolExecutor(
                pool.getCoreSize(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
    }

    @Bean
    public CorsFilter corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:10000"));
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "OPTIONS", "DELETE", "HEAD"));
        configuration.setAllowCredentials(true);

        configuration.setAllowedHeaders(Collections.singletonList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return new CorsFilter(source);
    }
}
