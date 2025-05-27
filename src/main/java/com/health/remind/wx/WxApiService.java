package com.health.remind.wx;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.health.remind.config.exception.DataException;
import com.health.remind.wx.entity.AccessToken;
import com.health.remind.wx.entity.Code2Session;
import com.health.remind.wx.entity.WXUserInfo;
import com.health.remind.wx.entity.WxMsg;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author qtx
 * @since 2025/4/26 21:08
 */
@Slf4j
@Component
public class WxApiService {

    private final RestClient restClient;

    private final ObjectMapper objectMapper;

    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.secret}")
    private String secret;

    public WxApiService(RestClient restClient, ObjectMapper objectMapper) {
        this.restClient = restClient;
        this.objectMapper = objectMapper;
    }

    public Code2Session getCode2Session(String code) {
        try {
            Code2Session body = restClient.get()
                    .uri("/sns/jscode2session?appid={appId}&secret={secret}&js_code={code}&grant_type=authorization_code",
                            appId, secret, code)
                    .retrieve()
                    .body(Code2Session.class);
            log.info("获取微信session成功: {}", body);
            return body;
        } catch (Exception e) {
            log.error("获取微信session失败", e);
            throw new DataException("获取微信session失败", 500);
        }
    }

    public Pair<String, WXUserInfo> decrypt(String encryptedData, String sessionKey, String iv) {
        try {
            String decrypt = WXDataDecrypt.decrypt(encryptedData, sessionKey, iv);
            return new Pair<>(decrypt, JSON.parseObject(decrypt, WXUserInfo.class));
        } catch (Exception e) {
            log.error("微信数据解密失败", e);
            throw new DataException("微信数据解密失败", 500);
        }
    }

    public AccessToken getAccessToken() {
        return restClient.get()
                .uri("/cgi-bin/token?grant_type=client_credential&appid={appid}&secret={secret}",
                        Map.of("appid", appId, "secret", secret))
                .accept(MediaType.TEXT_PLAIN)
                .retrieve()
                .body(AccessToken.class);
    }

    @SneakyThrows
    public void sendMsg(WxMsg msg) {
        String jsonString = objectMapper.writeValueAsString(msg);
        byte[] bytes = jsonString.getBytes(StandardCharsets.UTF_8);
        int length = bytes.length;
        log.info("发送微信消息: {},消息内容:{}", length, jsonString);
        try {
            ResponseEntity<Object> entity = restClient.post()
                    .uri("/cgi-bin/message/subscribe/send?access_token={access_token}",
                            Map.of("access_token", getAccessToken().getAccess_token()))
                    .headers(
                            httpHeaders -> httpHeaders.setContentLength(length))
                    .body(bytes)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .toEntity(Object.class);
            log.info("发送微信消息成功: {}", entity);
        } catch (Exception e) {
            log.error("发送微信消息失败", e);
            throw new DataException("发送微信消息失败", 500);
        }
    }

}
