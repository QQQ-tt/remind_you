package com.health.remind.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author QQQtx
 * @since 2025/2/17
 */
@Data
public class SysUserDTO {

    private Long id;

    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "电话")
    private String telephone;

    @Schema(description = "状态")
    private boolean status;

    @Schema(description = "角色id")
    private Long sysRoleId;
}
