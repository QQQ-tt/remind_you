package com.health.remind.pojo.dto;

import com.health.remind.common.enums.SysResourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author QQQtx
 * @since 2025/2/19
 */
@Data
public class SysResourceDTO {

    private Long id;

    @NotBlank(message = "资源名称不能为空")
    @Schema(description = "资源名称")
    private String name;

    @NotBlank(message = "接口地址/路由地址不能为空")
    @Schema(description = "接口地址/路由地址")
    private String url;

    @NotNull(message = "资源类型不能为空")
    @Schema(description = "资源类型：路由、接口")
    private SysResourceEnum type;

    @Schema(description = "方法类型")
    private String method;

    @Schema(description = "详细描述")
    private String description;

    @Schema(description = "资源状态")
    private Boolean status;

    @NotNull(message = "父id不能为空")
    @Schema(description = "父id")
    private Long parentId;
}
