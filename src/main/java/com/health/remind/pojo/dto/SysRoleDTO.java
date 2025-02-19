package com.health.remind.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author QQQtx
 * @since 2025/2/19
 */
@Data
public class SysRoleDTO {

    private Long id;

    @NotBlank(message = "角色名称不能为空")
    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色状态")
    private Boolean status = Boolean.FALSE;

    @Schema(description = "备注")
    private String remark;
}
