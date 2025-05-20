package com.health.remind.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qtx
 * @since 2025/2/16 20:51
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    private Long id;

    private String name;

    private String avatar;

    private String token;

    private Long expireTime;

    private boolean authorized;

    private Integer msgNum;

    public LoginVO(Long id, String name, Integer msgNum, String token, Long expireTime) {
        this.id = id;
        this.name = name;
        this.msgNum = msgNum;
        this.token = token;
        this.expireTime = expireTime;
    }
}
