package com.health.remind.pojo.dto;

import com.health.remind.config.PageDTO;
import com.health.remind.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qtx
 * @since 2025/2/16 21:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserPageDTO extends PageDTO<SysUser> {

    @NotBlank(message = "用户名不能为空")
    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "账户")
    private Long account;

    @Pattern(regexp = "/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\\d{8}$/", message = "手机号格式有误")
    @Schema(description = "电话")
    private String telephone;

    @Schema(description = "状态")
    private Boolean status;

    @Schema(description = "用户类型")
    private String userType;
}
