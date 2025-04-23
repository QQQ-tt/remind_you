package com.health.remind.pojo.dto;

import lombok.Data;

/**
 * @author qtx
 * @since 2025/4/23 12:52
 */
@Data
public class LoginAppDTO {

    private String code;

    private WxAppDTO userInfo;
}
