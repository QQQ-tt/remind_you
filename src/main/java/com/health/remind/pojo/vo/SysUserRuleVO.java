package com.health.remind.pojo.vo;

import com.health.remind.common.enums.InterestsLevelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author qtx
 * @since 2025/5/16 15:17
 */
@Data
public class SysUserRuleVO {

    private Long id;

    @Schema(description = "用户名称")
    private String name;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "账户")
    private Long account;

    @Schema(description = "微信openId")
    private String openId;

    @Schema(description = "权益等级：vip_0,vip_1 ")
    private InterestsLevelEnum interestsLevel;

    @Schema(description = "可用消息数量")
    private Integer msgNum;

    @Schema(description = "登录时间")
    private LocalDateTime loginTime;
}
