package com.health.remind.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qtx
 * @since 2025/2/16 21:41
 */
@Data
public class SysUserVO {

    private Long id;

    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "账户")
    private Long account;

    @Schema(description = "电话")
    private String telephone;

    @Schema(description = "是否启用")
    private Boolean status;

    @Schema(description = "角色名称")
    private String sysRoleName;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
