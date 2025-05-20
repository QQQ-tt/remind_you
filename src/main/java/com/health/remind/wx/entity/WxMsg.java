package com.health.remind.wx.entity;

import lombok.Data;

import java.util.Map;

/**
 * @author qtx
 * @since 2025/5/19 22:26
 */
@Data
public class WxMsg {

    private String template_id;

    private String touser;

    private String miniprogram_state = "formal";

    private String lang = "zh_CN";

    private Map<String, MsgInfo> data;
}
