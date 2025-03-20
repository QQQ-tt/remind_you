package com.health.remind.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author QQQtx
 * @since 2025/2/19
 */
@Data
public class SysRoleDTO {

    private Long id;

    @Size(min = 1, max = 50, message = "角色名称长度在1-50之间")
    @NotBlank(message = "角色名称不能为空")
    @Schema(description = "角色名称")
    private String name;

    @Schema(description = "角色状态")
    private Boolean status = Boolean.FALSE;

    @Size(max = 100, message = "角色名称长度在1-100之间")
    @Schema(description = "备注")
    private String remark;
}
