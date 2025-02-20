package com.health.remind.pojo.vo;

import com.health.remind.common.enums.SysResourceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author QQQtx
 * @since 2025/2/19
 */
@Data
public class SysResourceVO {

    private Long id;

    @Schema(description = "资源名称")
    private String name;

    @Schema(description = "接口地址/路由地址")
    private String url;

    @Schema(description = "资源类型：路由、接口")
    private SysResourceEnum type;

    @Schema(description = "方法类型")
    private String method;

    @Schema(description = "详细描述")
    private String description;

    @Schema(description = "资源状态")
    private Boolean status;
}
