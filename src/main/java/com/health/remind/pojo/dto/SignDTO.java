package com.health.remind.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @author qtx
 * @since 2025/2/14 22:07
 */
@Data
public class SignDTO {

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名")
    private String name;

    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式有误")
    @Schema(description = "电话")
    private String telephone;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;

    @NotBlank(message = "再次输入密码不能为空")
    @Schema(description = "再次输入密码")
    private String againPassword;
}
