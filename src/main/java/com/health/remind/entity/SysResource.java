package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.config.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统资源
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-18
 */
@Getter
@Setter
@TableName("sys_resource")
@Schema(name = "SysResource", description = "系统资源")
public class SysResource extends BaseEntity {

    @Schema(description = "资源名称")
    @TableField("name")
    private String name;

    @Schema(description = "接口地址/路由地址")
    @TableField("url")
    private String url;

    @Schema(description = "资源类型：菜单、接口等")
    @TableField("type")
    private String type;

    @Schema(description = "方法类型")
    @TableField("method")
    private String method;

    @Schema(description = "详细描述")
    @TableField("description")
    private String description;

    @Schema(description = "是否启用")
    @TableField("status")
    private Boolean status;

    @Schema(description = "父id")
    @TableField("parent_id")
    private Long parentId;
}
