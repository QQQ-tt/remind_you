package com.health.remind.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author qtx
 * @since 2025/4/23 12:52
 */
@Data
public class LoginAppDTO {

    @Schema(description = "微信code")
    private String code;

    @Schema(description = "用户信息")
    private String encryptedData;

    @Schema(description = "加密算法的初始向量")
    private String iv;
}
