package com.health.remind.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalTime;

/**
 * @author QQQtx
 * @since 2025/2/5 14:51
 */
@Data
public class FrequencyDetailDTO {

    private Long id;

    @NotNull(message = "频次id不能为空")
    private Long frequencyId;

    @Schema(description = "星期")
    private Integer frequencyWeekday;

    @Schema(description = "提醒时间(HH:mm)")
    private LocalTime frequencyTime;

    @Schema(description = "首次提醒时间设置(前)(HH:mm)")
    private LocalTime beforeRuleTime;

    @Schema(description = "首次提醒时间设置(后)(HH:mm)")
    private LocalTime afterRuleTime;
}
