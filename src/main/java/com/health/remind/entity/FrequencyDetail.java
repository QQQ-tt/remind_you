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

import java.time.LocalTime;

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
@AllArgsConstructor
@NoArgsConstructor
@TableName("frequency_detail")
@Schema(name = "FrequencyDetail", description = "频次详情表(时间明细表)")
public class FrequencyDetail extends BaseEntity {

    @TableField("frequency_id")
    private Long frequencyId;

    @TableField("frequency_weekday")
    private Integer frequencyWeekday;

    @Schema(description = "提醒时间(HH:mm)")
    @TableField("frequency_time")
    private LocalTime frequencyTime;

    @Schema(description = "首次提醒时间设置(前)(HH:mm)")
    @TableField("before_rule_time")
    private LocalTime beforeRuleTime;

    @Schema(description = "首次提醒时间设置(后)(HH:mm)")
    @TableField("after_rule_time")
    private LocalTime afterRuleTime;

    @Builder
    public FrequencyDetail(Long id, Long frequencyId, Integer frequencyWeekday, LocalTime frequencyTime,
                           LocalTime beforeRuleTime, LocalTime afterRuleTime) {
        super(id);
        this.frequencyId = frequencyId;
        this.frequencyWeekday = frequencyWeekday;
        this.frequencyTime = frequencyTime;
        this.beforeRuleTime = beforeRuleTime;
        this.afterRuleTime = afterRuleTime;
    }
}
