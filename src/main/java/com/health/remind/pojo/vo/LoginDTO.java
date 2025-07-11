package com.health.remind.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @author qtx
 * @since 2025/2/16 21:53
 */
@Data
public class LoginDTO {

    @Pattern(regexp = "^\\d{10}|^1[3-9]\\d{9}$", message = "账户格式不正确")
    @NotBlank(message = "账户不能为空")
    @Schema(description = "账户")
    private String account;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;
}
