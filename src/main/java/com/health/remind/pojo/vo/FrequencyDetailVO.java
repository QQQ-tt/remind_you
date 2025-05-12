package com.health.remind.pojo.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.health.remind.common.enums.FrequencyEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;

/**
 * @author QQQtx
 * @since 2025/2/5 14:49
 */
@Data
public class FrequencyDetailVO {

    private Long id;

    private Long frequencyId;

    private Integer frequencyWeekday;

    @Schema(description = "提醒时间(HH:mm)")
    private LocalTime frequencyTime;

    @Schema(description = "首次提醒时间设置(前)(HH:mm)")
    private LocalTime beforeRuleTime;

    @Schema(description = "首次提醒时间设置(后)(HH:mm)")
    private LocalTime afterRuleTime;

    @Schema(description = "时间字符串")
    private String frequencyTimeName;

    @JsonIgnore
    private FrequencyEnum cycleUnit;
}
