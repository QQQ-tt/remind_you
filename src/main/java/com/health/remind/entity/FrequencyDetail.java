package com.health.remind.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.health.remind.config.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 频次详情表(时间明细表)
 * </p>
 *
 * @author QQQtx
 * @since 2025-01-22
 */
@Getter
@Setter
@TableName("frequency_detail")
@Schema(name = "FrequencyDetail", description = "频次详情表(时间明细表)")
public class FrequencyDetail extends BaseEntity {

    @TableField("frequency_id")
    private Long frequencyId;

    @TableField("frequency_weekday")
    private Integer frequencyWeekday;

    @Schema(description = "提醒时间(HH:mm)")
    @TableField("frequency_time")
    private String frequencyTime;

    @Schema(description = "首次提醒时间设置(前)(HH:mm)")
    @TableField("before_rule_time")
    private String beforeRuleTime;

    @Schema(description = "首次提醒时间设置(后)(HH:mm)")
    @TableField("after_rule_time")
    private String afterRuleTime;
}
