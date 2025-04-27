package com.health.remind.wx;

import com.alibaba.fastjson.JSON;
import com.health.remind.config.exception.DataException;
import com.health.remind.wx.entity.Code2Session;
import com.health.remind.wx.entity.WXUserInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * @author qtx
 * @since 2025/4/26 21:08
 */
@Slf4j
@Component
public class WxApiService {

    private final RestClient restClient;

    @Value("${wx.appId}")
    private String appId;

    @Value("${wx.secret}")
    private String secret;

    public WxApiService(RestClient restClient) {
        this.restClient = restClient;
    }

    public Code2Session getCode2Session(String code) {
        try {
            return restClient.get()
                    .uri("/sns/jscode2session?appid={appId}&secret={secret}&js_code={code}&grant_type=authorization_code",
                            appId, secret, code)
                    .retrieve()
                    .body(Code2Session.class);
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
}
