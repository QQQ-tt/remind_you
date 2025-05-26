package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.config.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 角色资源关联表
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-18
 */
@Getter
@Setter
@TableName("sys_role_resource")
@Schema(name = "SysRoleResource", description = "角色资源关联表")
public class SysRoleResource extends BaseEntity {

    @TableField("sys_role_id")
    private Long sysRoleId;

    @TableField("sys_resource_id")
    private Long sysResourceId;
}
