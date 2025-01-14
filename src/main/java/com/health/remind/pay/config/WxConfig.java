package com.health.remind.pay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

/**
 * @author qtx
 * @since 2025/1/14 17:43
 */
@Configuration
public class WxConfig {

    @Bean
    public RestClient restClient() {
        TextPlainMessageConverter textPlainMessageConverter = new TextPlainMessageConverter();
        return RestClient.builder()
                .baseUrl("https://api.weixin.qq.com/")
                .messageConverters(converters -> {
                    List<HttpMessageConverter<?>> existingConverters = new ArrayList<>(converters);
                    existingConverters.add(textPlainMessageConverter);
                    converters.clear();
                    converters.addAll(existingConverters);
                })
                .build();
    }
}
