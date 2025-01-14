package com.health.remind.pay.config;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author qtx
 * @since 2025/1/14 17:42
 */
public class TextPlainMessageConverter extends AbstractHttpMessageConverter<Object> {

    public TextPlainMessageConverter() {
        super(new MediaType("text", "plain", StandardCharsets.UTF_8));
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return true; // 支持所有类
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException,
            HttpMessageNotReadableException {
        String json = new String(inputMessage.getBody()
                .readAllBytes(), StandardCharsets.UTF_8);
        return JSONObject.parseObject(json, clazz);
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        String json = JSONObject.toJSONString(o);
        outputMessage.getBody()
                .write(json.getBytes(StandardCharsets.UTF_8));
    }
}
