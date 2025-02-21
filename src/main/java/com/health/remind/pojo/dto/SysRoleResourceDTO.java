package com.health.remind.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author QQQtx
 * @since 2025/2/21
 */
@Data
public class SysRoleResourceDTO {

    @NotNull(message = "角色id不能为空")
    @Schema(description = "角色id")
    private Long sysRoleId;

    @Schema(description = "资源ids")
    private List<Long> sysResources;
}
