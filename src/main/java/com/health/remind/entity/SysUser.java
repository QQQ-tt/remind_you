package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.health.remind.common.enums.InterestsLevelEnum;
import com.health.remind.config.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

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

    @Schema(description = "头像")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "账户")
    @TableField("account")
    private Long account;

    @JsonIgnore
    @Schema(description = "密码")
    @TableField("password")
    private String password;

    @JsonIgnore
    @Schema(description = "电话")
    @TableField("telephone")
    private Long telephone;

    @Schema(description = "加密后的电话")
    @TableField("encrypted_telephone")
    private String encryptedTelephone;

    @Schema(description = "微信openId")
    @TableField("open_id")
    private String openId;

    @Schema(description = "消息数量")
    @TableField("msg_num")
    private Integer msgNum;

    @Schema(description = "用户信息")
    @TableField("user_info")
    private String userInfo;

    @Schema(description = "分享用户id")
    @TableField("shared_user_id")
    private Long sharedUserId;

    @Schema(description = "用户头像授权状态 0 拒绝 1 待同意 2 已同意")
    @TableField("authorized")
    private Integer authorized;

    @Schema(description = "是否启用")
    @TableField("status")
    private Boolean status;

    @Schema(description = "权益等级：vip_0,vip_1 ")
    @TableField("interests_level")
    private InterestsLevelEnum interestsLevel;

    @Schema(description = "用户类型")
    @TableField("user_type")
    private String userType;

    @Schema(description = "角色id")
    @TableField("sys_role_id")
    private Long sysRoleId;

    @Schema(description = "登录时间")
    @TableField("login_time")
    private LocalDateTime loginTime;

    @Builder
    public SysUser(Long id, String name, Long account, String password, String openId, String userInfo,
                   Integer authorized, Long telephone,
                   String encryptedTelephone,
                   Boolean status,
                   String userType,
                   Long sysRoleId,
                   LocalDateTime loginTime,
                   InterestsLevelEnum interestsLevel,
                   Long sharedUserId) {
        super(id);
        this.name = name;
        this.account = account;
        this.password = password;
        this.openId = openId;
        this.userInfo = userInfo;
        this.telephone = telephone;
        this.encryptedTelephone = encryptedTelephone;
        this.status = status;
        this.userType = userType;
        this.sysRoleId = sysRoleId;
        this.authorized = authorized;
        this.interestsLevel = interestsLevel;
        this.loginTime = loginTime;
         this.sharedUserId = sharedUserId;
    }
}
