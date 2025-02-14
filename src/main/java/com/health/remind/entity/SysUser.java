package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.config.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author QQQtx
 * @since 2025-02-14
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
@Schema(name = "SysUser", description = "")
public class SysUser extends BaseEntity {

    @Schema(description = "用户名称")
    @TableField("name")
    private String name;

    @Schema(description = "账户")
    @TableField("account")
    private Long account;

    @Schema(description = "密码")
    @TableField("password")
    private String password;

    @Schema(description = "电话")
    @TableField("telephone")
    private String telephone;

    @Schema(description = "是否启用")
    @TableField("status")
    private Boolean status;

    @Schema(description = "角色id")
    @TableField("sys_role_id")
    private Long sysRoleId;
}
