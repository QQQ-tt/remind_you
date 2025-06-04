package com.health.remind.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author qtx
 * @since 2025/6/4 9:45
 */
@Data
public class AppUserEmailDTO {

    @Size(max = 6, message = "验证码长度不能超过6个字符")
    @Schema(description = "验证码")
    private String captchaCode;

    @Size(max = 100, message = "邮箱长度不能超过100个字符")
    @Email(message = "邮箱格式不正确")
    @Schema(description = "邮箱")
    private String email;
}
