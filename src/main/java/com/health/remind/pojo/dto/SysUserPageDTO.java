package com.health.remind.pojo.dto;

import com.health.remind.config.PageDTO;
import com.health.remind.entity.SysUser;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author qtx
 * @since 2025/2/16 21:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysUserPageDTO extends PageDTO<SysUser> {

    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "账户")
    private Long account;

    @Schema(description = "电话")
    private String telephone;

    @Schema(description = "状态")
    private Boolean status;

    @NotBlank(message = "用户类型不能为空")
    @Schema(description = "用户类型：sys,user")
    private String userType;
}
