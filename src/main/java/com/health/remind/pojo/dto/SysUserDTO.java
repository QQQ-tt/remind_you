package com.health.remind.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author QQQtx
 * @since 2025/2/17
 */
@Data
public class SysUserDTO {

    private Long id;

    @Size(min = 1, max = 50, message = "用户名长度在1-50之间")
    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名称")
    private String name;

    @NotBlank(message = "电话不能为空")
    @Schema(description = "电话")
    private String telephone;

    @Schema(description = "密码")
    private String password;

    @Schema(description = "状态")
    private boolean status;

    @Schema(description = "角色id")
    private Long sysRoleId;
}
