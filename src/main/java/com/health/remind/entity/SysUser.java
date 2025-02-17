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
    private Long telephone;

    @Schema(description = "是否启用")
    @TableField("status")
    private Boolean status;

    @Schema(description = "用户类型")
    @TableField("user_type")
    private String userType;

    @Schema(description = "角色id")
    @TableField("sys_role_id")
    private Long sysRoleId;

    @Builder
    public SysUser(Long id, String name, Long account, String password, Long telephone, Boolean status,
                   String userType,
                   Long sysRoleId) {
        super(id);
        this.name = name;
        this.account = account;
        this.password = password;
        this.telephone = telephone;
        this.status = status;
        this.userType = userType;
        this.sysRoleId = sysRoleId;
    }
}
