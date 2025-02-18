package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.config.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 系统角色
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-18
 */
@Getter
@Setter
@TableName("sys_role")
@Schema(name = "SysRole", description = "系统角色")
public class SysRole extends BaseEntity {

    @Schema(description = "角色名称")
    @TableField("name")
    private String name;

    @Schema(description = "角色状态")
    @TableField("status")
    private Boolean status;

    @TableField("remark")
    private String remark;
}
