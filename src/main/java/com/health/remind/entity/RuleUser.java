package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.common.enums.RuleTypeEnum;
import com.health.remind.config.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author QQQtx
 * @since 2025-05-14
 */
@Getter
@Setter
@TableName("rule_user")
@Schema(name = "RuleUser", description = "")
public class RuleUser extends BaseEntity {

    @TableField("rule_template_id")
    private Long ruleTemplateId;

    @TableField("sys_user_id")
    private Long sysUserId;

    @Schema(description = "规则名称")
    @TableField("name")
    private String name;

    @Schema(description = "权益类型")
    @TableField("rule_type")
    private RuleTypeEnum ruleType;

    @Schema(description = "已使用数量")
    @TableField("use_value")
    private Integer useValue;

    @Schema(description = "默认值")
    @TableField("value")
    private Integer value;

    @Schema(description = "开始时间")
    @TableField("started_at")
    private LocalDateTime startedAt;

    @Schema(description = "过期时间")
    @TableField("expired_at")
    private LocalDateTime expiredAt;

    @Schema(description = "优先级，越小越先")
    @TableField("priority")
    private Integer priority;
}
