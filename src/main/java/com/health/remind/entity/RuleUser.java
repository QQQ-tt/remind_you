package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.config.BaseEntity;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

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

    @Schema(description = "已使用数量")
    @TableField("use_value")
    private Integer useValue;

    @Schema(description = "默认值")
    @TableField("value")
    private Integer value;

    @Schema(description = "过期时间")
    @TableField("expired_at")
    private LocalDateTime expiredAt;
}
