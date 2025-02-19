package com.health.remind.pojo.dto;

import com.health.remind.config.PageDTO;
import com.health.remind.entity.SysRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author QQQtx
 * @since 2025/2/19
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysRolePageDTO extends PageDTO<SysRole> {

    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "状态")
    private Boolean status;
}
