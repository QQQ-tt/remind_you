package com.health.remind.wx.entity;

import lombok.Data;

/**
 * @author qtx
 * @since 2025/4/26 21:53
 */
@Data
public class WXUserInfo {
    private String openId;
    private String nickName;
    private int gender;
    private String language;
    private String city;
    private String province;
    private String country;
    private String avatarUrl;
    private String unionId;
    private Watermark watermark;

    @Data
    public static class Watermark {
        private long timestamp;
        private String appid;
    }
}
