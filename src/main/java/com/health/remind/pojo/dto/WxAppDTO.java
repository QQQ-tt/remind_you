package com.health.remind.pojo.dto;

import lombok.Data;

/**
 * @author qtx
 * @since 2025/4/23 12:53
 */
@Data
public class WxAppDTO {

    private String avatarUrl;
    private String city;
    private String country;
    private Integer gender;
    private String language;
    private String nickName;
    private String province;
}
