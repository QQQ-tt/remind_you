package com.health.remind.wx.entity;

import lombok.Data;

/**
 * @author qtx
 * @since 2025/5/19 22:15
 */
@Data
public class AccessToken {

    private String access_token;

    private Integer expires_in;
}
