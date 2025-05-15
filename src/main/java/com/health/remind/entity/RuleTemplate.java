package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.common.enums.InterestsLevelEnum;
import com.health.remind.common.enums.RuleExpiredEnum;
import com.health.remind.common.enums.RuleTypeEnum;
import com.health.remind.config.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 规则模板
 * </p>
 *
 * @author QQQtx
 * @since 2025-05-14
 */
@Getter
@Setter
@TableName("rule_template")
@Schema(name = "RuleTemplate", description = "规则模板")
public class RuleTemplate extends BaseEntity {

    @Schema(description = "规则编码")
    @TableField("code")
    private String code;

    @Schema(description = "规则名称")
    @TableField("name")
    private String name;

    @Schema(description = "是否启用")
    @TableField("status")
    private Boolean status;

    @Schema(description = "默认值")
    @TableField("value")
    private Integer value;

    @Schema(description = "权益类型")
    @TableField("rule_type")
    private RuleTypeEnum ruleType;

    @Schema(description = "权益等级：vip_0,vip_1,ad_boost ")
    @TableField("interests_level")
    private InterestsLevelEnum interestsLevel;

    @Schema(description = "过期数值")
    @TableField("expired_period_value")
    private Integer expiredPeriodValue;

    @Schema(description = "过期单位")
    @TableField("expired_period_unit")
    private RuleExpiredEnum expiredPeriodUnit;

    @Schema(description = "过期时间类型：1累计时间，2指定时间")
    @TableField("expired_period_type")
    private Integer expiredPeriodType;

    @Schema(description = "优先级，越小越先")
    @TableField("priority")
    private Integer priority;

    @Schema(description = "描述")
    @TableField("description")
    private String description;
}
